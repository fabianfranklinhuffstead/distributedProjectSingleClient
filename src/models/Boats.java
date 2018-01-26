package models;

import java.awt.*;
import java.util.HashSet;

import controllers.CurrentRuntimeSession;
import controllers.GlobalResources;
import models.EnumsType.TypeOfGame;
import models.LoadingFiles.FileLoaderImage;
import views.Displays.DispGame.DisplayInGameBoat;

// Class boat, forward and backward movement, collisions and conversions
public class Boats
{
    // Speed ratio conversion
    private final float _SpeedRatioConversion;

    // speed value 
    private float _SpeedValue;

    // current angle based on 360•
    private float _AngleBasedOn360;

    // Behaviour Reverse mode
    private boolean _BehaviourReverse;

    // This is when on Island
    private boolean _BehaviourIsland;

    // When hits Rocks speed reduce to 0
    private boolean _BehaviourRocksCollision;

    // When hits boat speed reduce to 0
    private boolean _BehaviourCollisionWIthBoat;

    // boats image file index 
    private int _BoatIndexedImage;

    //This is the boat JLABEL display/ simple representation
    private DisplayInGameBoat _DisplayInGameBoat;

    // Hash set for current for key pressed
    private HashSet<Integer> _CurrentKeyPressed;

    // Public boats class has acceleration, speeds variable speed limits and pixel speeds. Also includes handling collisions
    public Boats(int _IndexOfBoatFile)
    {
        this._BoatIndexedImage = _IndexOfBoatFile;
        _SpeedRatioConversion = GlobalResources.ATTRIBUTESFORCONTROL_InGameMaxSpeed / GlobalResources.ATTRIBUTESFORCONTROL_MaxSpeed;
    }

    // Set current angle of boat
    public void set_AngleOfBoat(float _AngleOfBoat)
    {
        this._AngleBasedOn360 = _AngleOfBoat;
    }

    // Set current boat image 
    public void set_BoatIndexedImage(int _BoatIndexedImage) {
        this._BoatIndexedImage = _BoatIndexedImage;
    }

    // Loads boat images and returns into image array
    public Image[] ImageLoadersForBoat()
    {
        return FileLoaderImage.ImageLoad(_BoatIndexedImage);
    }

    // Method for handling key presses, no duplicates here
    public void KeyPressedHandlers(int keyCode) {
        // Use case on up pressed then follow this code
        if(keyCode == GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls[0] || keyCode == GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls[0] ) {
            _CurrentKeyPressed.add(keyCode); 
            return;
        }
        // Use case on back pressed then follow this code
        if(keyCode == GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls[1] || keyCode == GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls[1] ) {
            _CurrentKeyPressed.add(keyCode); 
            return;
        }
        // Use case on left pressed then follow this code
        if(keyCode == GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls[2] || keyCode == GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls[2] ) {
            TurningHandlers(keyCode, true);
            return;
        }
        // Use case on right pressed then follow this code
        if(keyCode == GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls[3] || keyCode == GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls[3] ) {
            TurningHandlers(keyCode,false);
        }
    }

    // Handles releases on keys
    public void KeyReleaseHandlers(int keyCode)
    {
        _CurrentKeyPressed.remove(keyCode);
    }

    // Handles turning as long as collision is not true for doing so
    private void TurningHandlers(int key, Boolean isLeft)
    {
        if (!_CurrentKeyPressed.contains(key)) {
            if (!CollisionTurnHandlers(_DisplayInGameBoat.GetBoatImageFromIndex(isLeft)))
            {
                _CurrentKeyPressed.add(key);
                int updatedDegreeIndex = _DisplayInGameBoat.Turn(isLeft);
                _AngleBasedOn360 = GlobalResources.BOATSINGAME_Angle[updatedDegreeIndex];
            }
            else {
                // Else the game engine is notified about collision and plays sound
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatImpact();
            }
        }
    }

    // Returns true if the boat collides using a new angle
    private boolean CollisionTurnHandlers(int CorrectedIndexOfAngle)
    {
        boolean returnedCollisionValue;
        Rectangle locationCurrent = _DisplayInGameBoat.getBounds();
        // Checks for crashing with other boats does not apply on one player mode
        if(CurrentRuntimeSession.get_GameType() != TypeOfGame.ONE_PLAYER)
        {
            returnedCollisionValue = CurrentRuntimeSession.get_ManagementOfCollision().boatCollision(this, locationCurrent, CorrectedIndexOfAngle);
            if (returnedCollisionValue)
                return true;
        }
        // Checks for hitting edges 
        returnedCollisionValue = CurrentRuntimeSession.get_ManagementOfCollision().outsideMapCollision(locationCurrent, CorrectedIndexOfAngle);
        return returnedCollisionValue;
    }

    // Calculates next frame by updating speed and location
    public void FormulationForIteratedFrame()
    {
        SpeedUpdate();
        FormulateUpdateLoation();
    }

    // Calculates new location
    private void FormulateUpdateLoation()
    {
        Point location = _DisplayInGameBoat.getLocation();

        // Switch statement for calculations on movement
        switch (Math.round(_AngleBasedOn360)) {
            // boats point up then move up Y+
            case 0:
                location.translate(0,CheckIfReverse(0 - Math.round(_SpeedValue) ));
                break;

            // boats point down then move up Y-
            case 180:
                location.translate(0, CheckIfReverse(Math.round(_SpeedValue)));
                break;

            // boats point left then move up X-
            case 90:
                location.translate(CheckIfReverse(Math.round(_SpeedValue)),0);
                break;

            // boats point right then move up X+
            case 270:
                location.translate(CheckIfReverse(0 - Math.round(_SpeedValue)), 0);
                break;
            default:
                // Will go going straight otherwise by default by using formulation
                location = ForumationForNewLocation(location);
                break;
        }
        //Checks for collision on updated values
        CollisionCheckerBehaviourHandlers(location); 
    }

    // Check for collision with Island, boat and edges
    private void CollisionCheckerBehaviourHandlers(Point locationNew)
    {
        Rectangle rectangle = GetRectangleBoundaries(locationNew);

        // Collision check is different for one player to two player modes
        if(CurrentRuntimeSession.get_GameType() != TypeOfGame.ONE_PLAYER)
        {
            _BehaviourCollisionWIthBoat = CurrentRuntimeSession.get_ManagementOfCollision().boatCollision(this, rectangle, GetIndexedCurrentAngle(_AngleBasedOn360));
            if (_BehaviourCollisionWIthBoat)
            {
                _SpeedValue = 0;
                // Pushes this info to game engine
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatCrash();
                return;
            }
        }

        // If boat hits the Rocks then the speed is reduce to 0 
        _BehaviourRocksCollision = CurrentRuntimeSession.get_ManagementOfCollision().outsideMapCollision(rectangle, GetIndexedCurrentAngle(_AngleBasedOn360));
        if (!_BehaviourRocksCollision) {
            // Collision sets location as it is
            _DisplayInGameBoat.setLocation(locationNew);
        } else {
            // In case of hitting the edges
            _SpeedValue = 0;
            // Call on boat impact
            GlobalResources.MainControl.get_RuntimeEngine().OnBoatImpact(); 
            return;
        }

        // The boat speed is reduce on non driving center area
        _BehaviourIsland = CurrentRuntimeSession.get_ManagementOfCollision().IslandCollision(rectangle,GetIndexedCurrentAngle(_AngleBasedOn360));
    }

    // Gets current angle and returns
    public int GetIndexedAngle()
    {
        return GetIndexedCurrentAngle(_AngleBasedOn360);
    }

    // Gets the angle in the array and returns
    private int GetIndexedCurrentAngle(float floatangle)
    {
        int i;
        for(i = 0; i < GlobalResources.BOATSINGAME_Angle.length; i++)
        {
            if (GlobalResources.BOATSINGAME_Angle[i] == floatangle)
                return i;
        }
        return i;
    }

    
    // Creates a rectangle to match the image size
    public Rectangle GetRectangleBoundaries() { return _DisplayInGameBoat.getBounds();
    }

    // This creates the starting private rectangle location and returns new value
    private Rectangle GetRectangleBoundaries(Point initialLocation)
    {
        return new Rectangle(initialLocation.x, initialLocation.y, GlobalResources.BOATSINGAME_SizeWidth ,GlobalResources.BOATSINGAME_SizeHeight);
    }

    // This checks for reverse speed and returns invert value if true
    private int CheckIfReverse(int updatedSpeed)
    {
        if (!_BehaviourReverse)
        {
            return updatedSpeed;
        }
        else
        {
            return (0 - updatedSpeed);
        }
    }

    // New location basing the the logic of formulation which is similar to how a X - Y graph is represented

    private Point ForumationForNewLocation(Point locationCurrent)
    {
        // This is for the angle radius
        double radianAngle = Math.toRadians(_AngleBasedOn360);
        // for the X axis formulation = SIN * speed is used
        double sine = Math.sin(radianAngle);
        double doubleX = _SpeedValue * sine;
        // for the Y axis formulation = COS * speed is used
        double cosine = Math.cos(radianAngle);
        double doubleY = 0- (_SpeedValue * cosine);

        locationCurrent.translate(CheckIfReverse((int) Math.round(doubleX)), CheckIfReverse((int) Math.round(doubleY)));
        return locationCurrent;
    }

    // Update speed method with if else statement for each corresponding method
    private void SpeedUpdate()
    {
        boolean onForwardButtonPress = _CurrentKeyPressed.contains(GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls[0]) || _CurrentKeyPressed.contains(GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls[0]);
        boolean onBackButtonPress = _CurrentKeyPressed.contains(GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls[1]) || _CurrentKeyPressed.contains(GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls[1]);
        boolean onForwardBackButtonPress = onForwardButtonPress && onBackButtonPress;
        boolean onForwardBackButtonNotPress = !(onForwardButtonPress || onBackButtonPress);
        boolean onStop = (_SpeedValue == 0);

        if(onForwardBackButtonPress || onForwardBackButtonNotPress)
        {
            BoatSlowMovement();
            return;
        }

        if (onForwardButtonPress && !_BehaviourReverse)
        {
            BoatAcceleration();
            return;
        }

        if (onForwardButtonPress && _BehaviourReverse)
        {
            if (onStop)
            {
                _BehaviourReverse = false;
                BoatAcceleration();
                return;
            }
            else
            {
                BoatSlowMovement();
                return;
            }
        }

        if (onBackButtonPress && _BehaviourReverse)
        {
            Accelerate(true, _BehaviourIsland);
            return;
        }

        if (onBackButtonPress && !_BehaviourReverse)
        {
            if (onStop)
            {
                _BehaviourReverse = true;
                Accelerate(true, _BehaviourIsland);
            }
            else
            {
                BoatSlowMovement();
            }
        }
    }

    // Accelerate method and rates of increase
    private void Accelerate(boolean reverseDirectionSpeed, boolean onIslandSpeed)
    {
        // If else depending on boat speed reverse or forward
        float MaxSpeed;
        if (!reverseDirectionSpeed)
            MaxSpeed = GlobalResources.ATTRIBUTESFORCONTROL_MaxSpeed;
        else
            MaxSpeed = GlobalResources.ATTRIBUTESFORCONTROL_MaxReverseSpeed;

        // Overrides speed when on Islands
        if (onIslandSpeed)
            MaxSpeed = GlobalResources.ATTRIBUTESFORCONTROL_MaxIslandSpeed;

        // Returns no speed if boat acceleration is not needed
        if (_SpeedValue == MaxSpeed)
        {
            GlobalResources.MainControl.get_RuntimeEngine().OnBoatAccelerationStop();
            return;
        }

        // Handles for speed quotas seen in shared resources
        if(_SpeedValue < MaxSpeed)
        {
            // boats initial start will give fixed boost
            if (_SpeedValue == 0) {
                _SpeedValue = GlobalResources.ATTRIBUTESFORCONTROL_MaxSpeed * GlobalResources.ATTRIBUTESFORCONTROL_ProgressiveStart50;
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatAcceleration();
            }
            // Continue behaviour on lower speeds
            else if (_SpeedValue < (GlobalResources.ATTRIBUTESFORCONTROL_MaxSpeed * GlobalResources.ATTRIBUTESFORCONTROL_ProgressiveStart25)) {
                _SpeedValue *= GlobalResources.ATTRIBUTESFORCONTROL_MajorAcceleration150;
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatAcceleration();
            }
            // Speeds are met in the middle at medium speeds
            else if (_SpeedValue < (GlobalResources.ATTRIBUTESFORCONTROL_MaxSpeed * GlobalResources.ATTRIBUTESFORCONTROL_NeutralAcceleration60)) {
                _SpeedValue *= GlobalResources.ATTRIBUTESFORCONTROL_NeutralAcceleration120;
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatAcceleration();
            }
            // On higher speeds speeds no longer increase and have limit
            else {
                _SpeedValue *= GlobalResources.ATTRIBUTESFORCONTROL_MajorAcceleration110;
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatAcceleration();
            }

            // This prevents it from going over a great way of managing the version of 1 - 10 speed ratio
            if(_SpeedValue > MaxSpeed)
            {
                _SpeedValue = MaxSpeed;
                GlobalResources.MainControl.get_RuntimeEngine().OnBoatAccelerationStop();
            }
        }

        // If speed is greater than speed allowed then it is slowed calling slow down
        if(_SpeedValue > MaxSpeed)
        {
            GlobalResources.MainControl.get_RuntimeEngine().OnBoatAccelerationStop();
            BoatSlowMovement();
        }
    }

    // Acceleration set to false on Islands
    private void BoatAcceleration()
    {
        Accelerate(false, _BehaviourIsland);
    }

    // Speed of boat is reduced if it is not = 0 and is multiplied by ratio
    private void BoatSlowMovement()
    {
        //Lower the speed, based on current speed.
        if(_SpeedValue <= GlobalResources.ATTRIBUTESFORCONTROL_DecreaseAcceleration5)
        {
            _SpeedValue = 0;
            _BehaviourReverse = false;
        }
        else
        {
            _SpeedValue *= GlobalResources.ATTRIBUTESFORCONTROL_DecreaseAcceleration90;
        }
    }

    // Gets boat display and returns this value
    public DisplayInGameBoat get_DisplayInGameBoat()
    {
        return _DisplayInGameBoat;
    }

    // Sets boat display
    public void set_ImagesForBoat(DisplayInGameBoat _BoatDisplay) {
        this._DisplayInGameBoat = _BoatDisplay;
        _BehaviourReverse = false;
        _BehaviourIsland = false;
        _BehaviourRocksCollision = false;
        _BehaviourCollisionWIthBoat = false;
        _SpeedValue = 0;
        _AngleBasedOn360 = 0;
        _CurrentKeyPressed = new HashSet<>(20);
    }
    
    // Gets the speed which is displayed for the HUD
    public int GetInGameSpeed()
    {
        int returnedRatio = (int)(_SpeedValue * _SpeedRatioConversion);

        // Shows maximum speed
        if(_SpeedValue >= GlobalResources.ATTRIBUTESFORCONTROL_MaxSpeed)
            returnedRatio = GlobalResources.ATTRIBUTESFORCONTROL_InGameMaxSpeed;

        // If boat collides with Rocks then result is 0
        if (_BehaviourRocksCollision)
            returnedRatio = 0;
        // If speed <= to threshold/showing limit then result is 0 on display
        if (_SpeedValue <= GlobalResources.ATTRIBUTESFORHUD_SpeedMask60)
            returnedRatio = 0;
            
        return returnedRatio;
    }

    // Gets and returns crash boat image
    public Image ImageCrashedLoaders()
    {
        return FileLoaderImage.ReturnCrashImage();
    }
}
