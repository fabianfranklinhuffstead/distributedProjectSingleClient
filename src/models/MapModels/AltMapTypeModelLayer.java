package models.MapModels;

import java.util.ArrayList;
import java.awt.geom.Line2D;

import controllers.CurrentRuntimeSession;
import models.EnumsType.ObjectTypeInGame;
import models.ManagementCollision.ObjectOfMap;
import views.MapViews.AltMapTypeView;


// Class medium map and loads map textures
public class AltMapTypeModelLayer extends GlobalMapModel {
    // boats starting points
    public AltMapTypeModelLayer() {
        Initial_Starting_Boat_Angles = 90;
        Initial_Boat_Starting_XValue_FirstPlayer = 375;
        Initial_Boat_Starting_YValue_FirstPlayer = 500;
        Initial_Boat_Starting_XValue_SecondPlayer = 375;
        Initial_Boat_Starting_YValue_SecondPlayer = 550;
        Map_HighResLowRes_Loader = "/mainImages/mapimages/AlternativeMap.png";
    }

    // Overrides map objects which can update 
    @Override
    protected void InitiateObjectsForMap() {
        _ObjectOfMaps = new ArrayList<>();
        AltMapTypeView ViewLayerForInGameMap = (AltMapTypeView) CurrentRuntimeSession.get_GlobalResourceOfMapView();
        ArrayList<Line2D> TemporaryLine;

        TemporaryLine = ViewLayerForInGameMap.GetLinesForGameTrack(true);
        InitiateOffMapObjects(TemporaryLine);
        TemporaryLine = ViewLayerForInGameMap.GetLinesForGameTrack(false);
        InitiateOffMapObjects(TemporaryLine);
    }

    // Creates array list of map objects
    private void InitiateOffMapObjects(ArrayList<Line2D> TemporaryLines) {
        ObjectOfMap temporary;
        for (Line2D TemporaryLine : TemporaryLines) {
            temporary = new ObjectOfMap();
            temporary.LineValue = TemporaryLine;
            temporary.TypeInGame = ObjectTypeInGame.AREA_OUT_OF_BOUNDS;
            _ObjectOfMaps.add(temporary);
        }
    }


}
