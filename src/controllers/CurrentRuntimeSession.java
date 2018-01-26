package controllers;

import java.util.ArrayList;

import models.Players;
import models.EnumsType.TypeOfGame;
import models.ManagementCollision.ManagementOfCollision;
import models.MapModels.GlobalMapModel;
import views.MapViews.GlobalResourceOfMapView;


// This is the current game session with all objects private and public relating
public class CurrentRuntimeSession
{
    //This represent game mode one player or two player
    private static TypeOfGame _GameType = TypeOfGame.NO_GAME_SELECTED;
    //This represents players 1, 2
    private static ArrayList<Players> _PlayerAmount;
    //The string name for map; Standard, Alternative (please refer to GlobalResources.ATTRIBUTESFORMAP_MapTitlesObject)
    private static String _MapTypeName;
    //The collision manager
    private static ManagementOfCollision _ManagementOfCollision;
    //This is the view for the map
    private static GlobalResourceOfMapView _GlobalResourceOfMapView; 
    //This is the map model for further collision detection.
    private static GlobalMapModel _ModelOfMap; 

    //This simply gets and returns map view
    public static GlobalResourceOfMapView get_GlobalResourceOfMapView() {
        return _GlobalResourceOfMapView;
    }

    //This sets the mapview and is CALLED by GAME ENGINE
    public static void set_ViewOfMap(GlobalResourceOfMapView _GlobalResourceOfMapView) {
        CurrentRuntimeSession._GlobalResourceOfMapView = _GlobalResourceOfMapView;
    }

    //This simply gets and returns map model
    public static GlobalMapModel get_ModelOfMap() {
        return _ModelOfMap;
    }
    
    //This sets the map view and is CALLED by GAME ENGINE
    public static void set_MapModel(GlobalMapModel _ModelOfMap) {
        CurrentRuntimeSession._ModelOfMap = _ModelOfMap;
    }

    //This simply gets and returns Game mode i.e. single or multi
    public static TypeOfGame get_GameType() {
        return _GameType;
    }

    //This sets the game mode and is CALLED by GAME ENGINE
    public static void set_GameType(TypeOfGame _TypeGameOf) {
        CurrentRuntimeSession._GameType = _TypeGameOf;
        //switch case used here taking game mode
        switch (_TypeGameOf) {
            case NO_GAME_SELECTED:
                break;
            case ONE_PLAYER:
                _PlayerAmount = new ArrayList<>();
                _PlayerAmount.add(new Players("Players 1"));
            case TWO_PLAYER:
                _PlayerAmount = new ArrayList<>();
                _PlayerAmount.add(new Players("Players 1"));
                _PlayerAmount.add(new Players("Players 2"));
                break;
        }
    }

    //This simply gets and returns list of players; 0, 1.
    public static ArrayList<Players> get_PlayerAmount() {
        return _PlayerAmount;
    }

    //This simply gets and returns selected map based on string; Standard or Alternative.
    public static String get_SelectionMapTypeName() {
        return _MapTypeName;
    }

    //This sets the map name and is concatenated to current game session. 
    public static void set_SelectedMapTypeName(String _SelectedMapTypeName)
    {
        CurrentRuntimeSession._MapTypeName = _SelectedMapTypeName;
    }

    //This simply gets and returns the collision manager object.
    public static ManagementOfCollision get_ManagementOfCollision() {
        return _ManagementOfCollision;
    }

    //This sets the collision manager and is concatenated to current game session. 
    public static void set_ManagementOfCollision(ManagementOfCollision _ManagementOfCollision) {
        CurrentRuntimeSession._ManagementOfCollision = _ManagementOfCollision;
    }

    //Method for reseting game session
    public static void RestartSessionOfGame()
    {
        _GameType = TypeOfGame.NO_GAME_SELECTED;
        _MapTypeName = "";
        _PlayerAmount = null;
        _ManagementOfCollision = null;
        _ModelOfMap = null;
        _GlobalResourceOfMapView = null;
    }
}
