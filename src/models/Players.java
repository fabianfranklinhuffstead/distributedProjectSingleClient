package models;

import java.awt.*;

// Players class (using boat)
public class Players
{

    // In game boat
    private final Boats _Boats;

    // Players string
    private String _PlayerTitles = "";
    
    // Players constructor
    public Players(String _PlayerTitles)
    {
        this._PlayerTitles = _PlayerTitles;
        
        // boats is set to -1 because no one knows the given index used
        _Boats = new Boats(-1);
    }

    // Gets and returns players name/title
    public String get_PlayerTitles()
    {
        return _PlayerTitles;
    }

    // Gets and returns player boat
    public Boats get_Boats()
    {
        return _Boats;
    }

    // Gets and returns player small image for HUD
    public Image GetHUDForPlayers()
    {
        Image result = null;
        if(_Boats != null)
        {
            result = _Boats.get_DisplayInGameBoat().GetInitialImageForBoatHUD();
        }
        return  result;
    }

    //Gets and return the speed for boat
    public int GeUpdatedSpeedForHUD()
    {
        int result = 0;
        if(_Boats != null)
        {
            result = _Boats.GetInGameSpeed();
        }
        return  result;
    }
}
