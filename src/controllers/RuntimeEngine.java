package controllers;


import javax.swing.*;
import java.util.Objects;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import models.ManagementCollision.ManagementOfCollision;
import models.MapModels.AltMapTypeModelLayer;
import models.MapModels.StandardMapTypeModelLayer;
import views.AudioEngine;
import views.CoreSwingUIComponent;
import views.MapViews.AltMapTypeView;
import views.MapViews.StandardMapTypeView;

//Manages map, boats and other game mechanics
public class RuntimeEngine implements ActionListener {
    //Creation of the JFRAME which loads of JPANELS
    private final CoreSwingUIComponent _CoreSwingUIComponent;

    //Creation of the Sound engine
    private final AudioEngine _AudioEngine;

    //Creation of timer which is triggered at runtime to track changes
    private Timer _TimerFrameRate;

     //Constructor for game engine UICore for JFFRAME and sound engine for music/audios
    public RuntimeEngine(CoreSwingUIComponent CoreSwingUIComponent, AudioEngine AudioEngine)
    {
        _CoreSwingUIComponent = CoreSwingUIComponent;
        _AudioEngine = AudioEngine;
    }

     //Starts the game with predefined configurations
    public void StartGame()
    {
        //Initiated selected map
        ApplyMapSelection();

        //Initiates collision
        ManagementOfCollision moc = new ManagementOfCollision(CurrentRuntimeSession.get_PlayerAmount(), CurrentRuntimeSession.get_ModelOfMap().GetObjectofMap());
        CurrentRuntimeSession.set_ManagementOfCollision(moc);

        //Initiates navigation to in game screen
        _CoreSwingUIComponent.InGameDisplayNavigation();

        //Creates new timer and sets to event handler
        _TimerFrameRate = new Timer(GlobalResources.GAMEFRAMERATE, this); 
        _TimerFrameRate.start();
    }

    //Starts the selected map
    private void ApplyMapSelection() {
        if (Objects.equals(CurrentRuntimeSession.get_SelectionMapTypeName(), GlobalResources.ATTRIBUTESFORMAP_MapTitlesObject[0])) {
            //returns selected map based on current game session
            CurrentRuntimeSession.set_MapModel(new StandardMapTypeModelLayer());
            CurrentRuntimeSession.set_ViewOfMap(new StandardMapTypeView());
            return;
        }

        if (Objects.equals(CurrentRuntimeSession.get_SelectionMapTypeName(), GlobalResources.ATTRIBUTESFORMAP_MapTitlesObject[1])) {
            CurrentRuntimeSession.set_MapModel(new AltMapTypeModelLayer());
            CurrentRuntimeSession.set_ViewOfMap(new AltMapTypeView());
        }
    }


    @Override
    //Updates screen each with each frame based on timer
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == _TimerFrameRate) {
            _CoreSwingUIComponent.UpdateInGameDisplay();
        }
    }

    //Sound for boat impact
    public void OnBoatImpact()
    {
        if (GlobalResources.ATTRIBUTESFORDEFAULT_SoundState)
        {
            _AudioEngine.AudioPlayBoatImpact();
        }
    }

    //Sound for acceleration
    public void OnBoatAcceleration()
    {
        if (GlobalResources.ATTRIBUTESFORDEFAULT_SoundState)
        {
            _AudioEngine.AudioBoatAccelerator(true);
        }
    }

    //boats acceleration not true
    public void OnBoatAccelerationStop()
    {
        if (GlobalResources.ATTRIBUTESFORDEFAULT_SoundState)
        {
            _AudioEngine.AudioBoatAccelerator(false);
        }
    }

    
    //Calls crash sound, stops frames, switches  labels to crashed icons and ends game.
    public void OnBoatCrash()
    {
        _TimerFrameRate.stop();
        _CoreSwingUIComponent.get_InGameDisplay().OnBoatCrashSetImage();

        if (GlobalResources.ATTRIBUTESFORDEFAULT_SoundState)
        {
            _AudioEngine.AudioBoatCrash();
        }

        GameComplete();
    }

    //On game end frame stops, display pop up and removes old game screen allowing user to n back
    private void GameComplete()
    {
        _TimerFrameRate.stop();
        _CoreSwingUIComponent.GameOverDisplayMenu();
        _CoreSwingUIComponent.DestroyDisplayInGame();
        GlobalResources.MainControl.NavigatingBackToMenuMainDisplay();
    }

    //Shutdown stops frame and removes old JPANELS
    public void ExitApplication()
    {
        _TimerFrameRate.stop();
        _CoreSwingUIComponent.DestroyDisplayInGame();
    }
}
