package views.Displays.DispGame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.stream.Stream;
import java.util.Arrays;

import controllers.CurrentRuntimeSession;
import controllers.GlobalResources;
import models.EnumsType.TypeOfGame;


// In game JPANEL with has action listener and key listener events
@SuppressWarnings("serial")
public class GameDisplay extends JPanel implements ActionListener, KeyListener
{

    // boats in game display label
    private DisplayInGameBoat[] _BoatImage;
    private DisplayHUD[] _DisplayHUD;
    private JLabel _HighResLowResMapTextures = null;

    // In game screen defaults
    public GameDisplay()
    {
        this.setLayout(null);
        BoatTitles();
        HUDInitiator();
        addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.requestFocus();
        this.setVisible(true);
    }

    // Private method for in game on shutdown
    public void GameShutDown()
    {
        for (int i = 0; i < _BoatImage.length; i++) {
           _BoatImage[i].GameShutDown();
            _DisplayHUD[i] = null;
            _HighResLowResMapTextures = null;
        }
    }


    // Private method for in game display HUDS
    private void HUDInitiator()
    {
        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.ONE_PLAYER)
        {
            _DisplayHUD = new DisplayHUD[1];
            _DisplayHUD[0] = new DisplayHUD(CurrentRuntimeSession.get_PlayerAmount().get(0));
            _DisplayHUD[0].setLocation(GlobalResources.ATTRIBUTESFORHUD_OnePlayerXPoistion, GlobalResources.ATTRIBUTESFORHUD_OnePlayerYPoistion);
            _DisplayHUD[0].DisplayHUDAttributes();
        }

        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.TWO_PLAYER)
        {
            _DisplayHUD = new DisplayHUD[2];
            _DisplayHUD[0] = new DisplayHUD(CurrentRuntimeSession.get_PlayerAmount().get(0));
            _DisplayHUD[1] = new DisplayHUD(CurrentRuntimeSession.get_PlayerAmount().get(1));
            _DisplayHUD[0].setLocation(GlobalResources.ATTRIBUTESFORHUD_OnePlayerXPoistion, GlobalResources.ATTRIBUTESFORHUD_OnePlayerYPoistion);
            _DisplayHUD[1].setLocation(GlobalResources.ATTRIBUTESFORHUD_SecondPlayerXPoistion, GlobalResources.ATTRIBUTESFORHUD_SecondPlayerYPoistion);
            _DisplayHUD[0].DisplayHUDAttributes();
            _DisplayHUD[1].DisplayHUDAttributes();
        }

        Stream.of(_DisplayHUD).forEach(h ->
        {
            this.add(h);
            h.setVisible(true);
        });
    }

    // Private method for in game current session boat locations, starter other starter configurations
    private void BoatTitles()
    {
        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.ONE_PLAYER)
        {
            _BoatImage = new DisplayInGameBoat[1];
            _BoatImage[0] = new DisplayInGameBoat(CurrentRuntimeSession.get_PlayerAmount().get(0));

            _BoatImage[0].setLocation(CurrentRuntimeSession.get_ModelOfMap().getInitial_Boat_Starting_XValue_FirstPlayer(), CurrentRuntimeSession.get_ModelOfMap().getInitial_Boat_Starting_YValue_FirstPlayer());
            _BoatImage[0].SetInitialImage(CurrentRuntimeSession.get_ModelOfMap().getInitial_Starting_Boat_Angles());
        }

        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.TWO_PLAYER)
        {
            _BoatImage = new DisplayInGameBoat[2];
            _BoatImage[0] = new DisplayInGameBoat(CurrentRuntimeSession.get_PlayerAmount().get(0));
            _BoatImage[1] = new DisplayInGameBoat(CurrentRuntimeSession.get_PlayerAmount().get(1));


            _BoatImage[0].setLocation(CurrentRuntimeSession.get_ModelOfMap().getInitial_Boat_Starting_XValue_FirstPlayer(), CurrentRuntimeSession.get_ModelOfMap().getInitial_Boat_Starting_YValue_FirstPlayer());
            _BoatImage[0].SetInitialImage(CurrentRuntimeSession.get_ModelOfMap().getInitial_Starting_Boat_Angles());
            _BoatImage[1].setLocation(CurrentRuntimeSession.get_ModelOfMap().getInitial_Boat_Starting_XValue_SecondPlayer(), CurrentRuntimeSession.get_ModelOfMap().getInitial_Boat_Starting_YValue_SecondPlayer());
            _BoatImage[1].SetInitialImage(CurrentRuntimeSession.get_ModelOfMap().getInitial_Starting_Boat_Angles());

        }



        Stream.of(_BoatImage).forEach(c ->
        {
            this.add(c);
            c.setVisible(true);
        });
    }

    // Callback to iterate through HUDS and boats
    public void IterateFrame()
    {
        Arrays.stream(_BoatImage).forEach(ci ->
        {
            if(GlobalResources.MainControl.get_RuntimeEngine() != null)
            {
                ci.NewIteratedFrame();
            }
        });

        Arrays.stream(_DisplayHUD).forEach(dh ->
        {
            if(GlobalResources.MainControl.get_RuntimeEngine() != null) {
                dh.DisplayHUDUpdates();
            }
        });

        if(GlobalResources.MainControl.get_RuntimeEngine() != null)
            repaint();
    }

    // Override method for when drawing map views and textures
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        DrawMapToDisplay(g);
    }

    private void DrawMapToDisplay(Graphics g)
    {
        if (GlobalResources.ATTRIBUTESFORDEFAULT_HighResLowResState)
        {
            if (_HighResLowResMapTextures == null)
            {
                _HighResLowResMapTextures = new JLabel();
                Icon i = new ImageIcon(CurrentRuntimeSession.get_GlobalResourceOfMapView().ImageTextureLoader());
                _HighResLowResMapTextures.setIcon(i);
                _HighResLowResMapTextures.setSize(i.getIconWidth(), i.getIconHeight());
                _HighResLowResMapTextures.setLocation(0, 0);
                this.add(_HighResLowResMapTextures);
            }
            else
            {
                if (!_HighResLowResMapTextures.isVisible()) {
                    _HighResLowResMapTextures.setVisible(true);
                }
            }
        } else {
            if (_HighResLowResMapTextures != null) {

                _HighResLowResMapTextures.setVisible(false);
            }
            CurrentRuntimeSession.get_GlobalResourceOfMapView().DrawMap(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        }

    @Override
    public void keyTyped(KeyEvent e)
    {
        }

    // Override method for when key is pressed
    @Override
    public void keyPressed(KeyEvent e)
    {
        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.ONE_PLAYER)
        {
            if(Arrays.asList(GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls).contains(e.getKeyCode()))
            {
                // Prints message dialouge for key pressed
                _BoatImage[0].ControlKeyPressed(e.getKeyCode());
            }

        }
        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.TWO_PLAYER)
        {
            if(Arrays.asList(GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls).contains(e.getKeyCode()))
            {
                // Prints message dialogue for key pressed
                _BoatImage[0].ControlKeyPressed(e.getKeyCode());
            }

            if(Arrays.asList(GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls).contains(e.getKeyCode()))
            {
                // Prints message dialogue for key pressed
                _BoatImage[1].ControlKeyPressed(e.getKeyCode());
            }

        }

    }

    // Override method for when key is released
    @Override
    public void keyReleased(KeyEvent e)
    {
        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.ONE_PLAYER)
        {
            if(Arrays.asList(GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls).contains(e.getKeyCode()))
            {
                // Prints message dialogue for key released
                _BoatImage[0].ControlKeyReleased(e.getKeyCode());
            }
        }
        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.TWO_PLAYER)
        {
            if(Arrays.asList(GlobalResources.ATTRIBUTESFORCONTROL_OnePlayerControls).contains(e.getKeyCode()))
            {
                // Prints message dialogue for key released
                _BoatImage[0].ControlKeyReleased(e.getKeyCode());
            }

            if(Arrays.asList(GlobalResources.ATTRIBUTESFORCONTROL_TwoPlayerControls).contains(e.getKeyCode()))
            {
                // Prints message dialogue for key released
                _BoatImage[1].ControlKeyReleased(e.getKeyCode());
            }
        }
    }

    // Occurs when the boats crash
    public void OnBoatCrashSetImage()
    {
        for (DisplayInGameBoat _BoatLabel : _BoatImage) {
            _BoatLabel.SetImageCrash();
        }

        repaint();
    }


}
