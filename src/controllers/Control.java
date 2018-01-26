package controllers;

import models.EnumsType.TypeOfGame;
import views.AudioEngine;
import views.CoreSwingUIComponent;

  /** MAIN CONTROLLER 
   *  This is the main controller of the application 
   *  this controls a lot of the main functionality for models and views "MVC" 
   */
public class Control {

    //Sound engine for game
    private AudioEngine _AudioEngine;

    //Control game states
    private RuntimeEngine _RuntimeEngine; 

    //This is the SWINGUI JFrame it displays other JPANELS
    private CoreSwingUIComponent _CoreSwingUIComponent;

    //Application start up, this starts the application
    public void StartApplication()
    {
        //Creates new SWINGUICore and initiates
        _CoreSwingUIComponent = new CoreSwingUIComponent();
        _CoreSwingUIComponent.DefaultWindowCreation();

        //This navigates to the main menu.
        _CoreSwingUIComponent.NavigateToMenuMainDisplay();

        //Creates a new sound engine
        _AudioEngine = new AudioEngine();
    }
    
    //On instance of selecting a two player instance of the game
    public void UserStartsTwoPlayerGame()
    {
        CurrentRuntimeSession.set_GameType(TypeOfGame.TWO_PLAYER);
        DefaultsWhichGoToBoatSelectionDisplay();
    }

    //On instance of selecting a single player instance of the game
    public void UserStartsOnePlayerGame()
    {
        CurrentRuntimeSession.set_GameType(TypeOfGame.ONE_PLAYER);
        DefaultsWhichGoToBoatSelectionDisplay();
    }
    
    
    //On instance of selecting which type of boat
    private void DefaultsWhichGoToBoatSelectionDisplay() {
        _CoreSwingUIComponent.NavigateToLaunchDisplay();
        _CoreSwingUIComponent.SelectDefaultValuesOnLaunchDisplay(); //default instance created here
    }

    //On instance of either end game or new game selected
    public void NavigatingBackToMenuMainDisplay() {
        if(_RuntimeEngine != null)
        {
            //if game engine running shutdown (hard reset)
            _RuntimeEngine.ExitApplication();
            _RuntimeEngine = null;
        }
        //This resets game session
        CurrentRuntimeSession.RestartSessionOfGame();

        // Navigate to main screen
        _CoreSwingUIComponent.NavigateToMenuMainDisplay(); 
    }

    //On instance of game session sets boat and player starting at 1 for player and 0 for boat
    public void ChangeBoatSession(int AmountOfPlayers, int indexOfBoat)
    {
        CurrentRuntimeSession.get_PlayerAmount().get(AmountOfPlayers-1).get_Boats().set_BoatIndexedImage(indexOfBoat);
    }

    //On instance of selecting map sets map this starts from 0
    public void ChangeMapSession(int MapType)
    {
        CurrentRuntimeSession.set_SelectedMapTypeName(GlobalResources.ATTRIBUTESFORMAP_MapTitlesObject[MapType]);
    }

    //Launches and starts actual game and not session
    public void LaunchGame()
    {
        _RuntimeEngine = new RuntimeEngine(_CoreSwingUIComponent, _AudioEngine);
        _RuntimeEngine.StartGame();
    }

   /** SETS the following sets are for options
    *  Check comments for the following instructions for each.
    */
    //Sets background music state. True = on; False = off.
    public void BoatGameAudioMenuBar(boolean currentState)
    {
        GlobalResources.ATTRIBUTESFORDEFAULT_MusicState = currentState;
        _AudioEngine.SetdMusicAudio(currentState);
    }

    //Sets background audio state. True = on; False = off.
    public void BoatGameSoundMenuBar(boolean currentState)
    {
        GlobalResources.ATTRIBUTESFORDEFAULT_SoundState = currentState;
    }
    
   //Sets map textures state. True = on; False = off.
    public void BoatGameMapTextureMenuBar(boolean currentState)
    {
        GlobalResources.ATTRIBUTESFORDEFAULT_HighResLowResState = currentState;
    }

    //Simple get method which returns game engine
    public RuntimeEngine get_RuntimeEngine() {
        return _RuntimeEngine;
    }

    //Shuts down application using dispose method if user clicks on binded property.
    public void BoatGameUserExit()
    {
        _AudioEngine.SetdMusicAudio(false);
        _CoreSwingUIComponent.dispose();
    }
}
