package views.Displays.DispLaunch;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import controllers.CurrentRuntimeSession;
import controllers.GlobalResources;
import models.EnumsType.TypeOfGame;
import views.Displays.DispLaunch.*;


// Launch screen JPANEL for both map and boat
@SuppressWarnings({ "serial", "unused" })
public class DisplayLaunch extends JPanel implements ActionListener
{

    // This is the background image 
    private final Image _ImageBackground;

    // Array list for boat selection panel.
    private ArrayList<SelectionBoatDisplay> _SelectionBoatDisplay;
    
    // This is the map selection JPANEL for maps
    private SelectionMapDisplay _SelectionMapDisplay;
    
    // Main menu button
    private JButton _ButtonMainMenu;
    
    // Button for starting game
    private JButton _ButtonStartGame;

    // Launch Screen with JPANEL functionality
    public DisplayLaunch (Image backgroundImage)
    {
        this.setLayout(null);
        _ImageBackground = backgroundImage;

        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.ONE_PLAYER)
        {
            SetOnePlayerSelectionBG();
        }

        if(CurrentRuntimeSession.get_GameType() == TypeOfGame.TWO_PLAYER)
        {
            SetTwoPlayerSelectionBG();
        }

        AddButtonForMainMenu();
        AddButtonForStartingGame();
        this.setVisible(true);
    }
    
    // Default values for selections game type and boat selections
    public void DefaultValuesSet() {
        if (CurrentRuntimeSession.get_GameType() == TypeOfGame.ONE_PLAYER) {
            _SelectionBoatDisplay.get(0).BoatSelectIndex(0);
        }

        if (CurrentRuntimeSession.get_GameType() == TypeOfGame.TWO_PLAYER) {
            _SelectionBoatDisplay.get(1).BoatSelectIndex(1);
        }

        _SelectionMapDisplay.DefaultMapSelection();

        setVisible(true);
        repaint();
    }

    // The start game/Go button in given location and default settings (find in shared resources)
    private void AddButtonForStartingGame()
    {
        if(_ButtonStartGame == null)
        {
           _ButtonStartGame = new JButton(GlobalResources.PREPDISPLAYATTRIBUTES_GoButtonTitle);

            int location_x = _SelectionMapDisplay.getX();
           _ButtonStartGame.setLocation(location_x, GlobalResources.PREPDISPLAYATTRIBUTES_GoButtonYLocation);

            int width = _SelectionMapDisplay.getWidth();
            _ButtonStartGame.setSize(width,GlobalResources.PREPDISPLAYATTRIBUTES_GoButtonHeight);
            _ButtonStartGame.setBackground(GlobalResources.PREPDISPLAYATTRIBUTES_ColorGoButton);
            _ButtonStartGame.setFont(GlobalResources.PREPDISPLAYATTRIBUTES_FontStylingGoButton);

            _ButtonStartGame.setForeground(GlobalResources.PREPDISPLAYATTRIBUTES_ColorFrontGoButton);

            this.add(_ButtonStartGame);
            _ButtonStartGame.setVisible(true);
            _ButtonStartGame.addActionListener(this);
        }

    }

    // The return to main menu button in given location and default settings (find in shared resources)
    private void AddButtonForMainMenu()
    {
        if(_ButtonMainMenu == null)
        {
            _ButtonMainMenu = new JButton(GlobalResources.PREPDISPLAYATTRIBUTES_MainMenuButton);
            _ButtonMainMenu.setLocation(GlobalResources.PREPDISPLAYATTRIBUTES_MainMenuXLocation,GlobalResources.PREPDISPLAYATTRIBUTES_MainMenuYLocation);
            _ButtonMainMenu.setSize(GlobalResources.PREPDISPLAYATTRIBUTES_WidthMainMenuButton,GlobalResources.PREPDISPLAYATTRIBUTES_HeightMainMenuButton);

            _ButtonMainMenu.setBackground(GlobalResources.PREPDISPLAYATTRIBUTES_ColorMainMenuButton);
            _ButtonMainMenu.setFont(GlobalResources.PREPDISPLAYATTRIBUTES_FontStylingMainMenuButton);

            this.add(_ButtonMainMenu);
            _ButtonMainMenu.setVisible(true);

            _ButtonMainMenu.addActionListener(this);
        }
    }

    // Draws background image and paints
    protected void  paintComponent(Graphics g)
    {
        super.paintComponent(g);
        try
        {
            g.drawImage(_ImageBackground,0,0,this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Single player use case with panels
    private void SetOnePlayerSelectionBG()
    {
        // The SelectionboatDisplay
        _SelectionBoatDisplay = new ArrayList<>();
        SelectionBoatDisplay selectionBoatDisplay = new SelectionBoatDisplay(GlobalResources.First_Player);
        _SelectionBoatDisplay.add(selectionBoatDisplay);

        this.add(selectionBoatDisplay);
        selectionBoatDisplay.setLocation(GlobalResources.ATTRIBUTESFORBOAT_Player1XLocation,GlobalResources.ATTRIBUTESFORBOAT_Player1YLocation);
        selectionBoatDisplay.setVisible(true);
        selectionBoatDisplay.DefaultBoatSelection();


        // Keyboard label
        AssignKeyboard keyBoardLayout = new AssignKeyboard(GlobalResources.First_Player);
        int keyBoardLayout_X = GlobalResources.ATTRIBUTESFORBOAT_Player1XLocation + selectionBoatDisplay.getWidth() + GlobalResources.ATTRIBUTESFORHELP_SpacingXLocation;
        int keyBoardLayout_Y = GlobalResources.ATTRIBUTESFORBOAT_Player1YLocation;
        keyBoardLayout.setLocation(keyBoardLayout_X,keyBoardLayout_Y);
        this.add(keyBoardLayout);

        // Map selection panel
        SelectionMapDisplay selectionMapDisplay = new SelectionMapDisplay();
        int mapSelectionPanel_X = keyBoardLayout_X + keyBoardLayout.getWidth() +  GlobalResources.ATTRIBUTESFORMAP_Spacing;
        selectionMapDisplay.setLocation(mapSelectionPanel_X, keyBoardLayout_Y);

        this.add(selectionMapDisplay);
        _SelectionMapDisplay = selectionMapDisplay;
    }

    // Two player use case with panels
    private void SetTwoPlayerSelectionBG()
    {
        // The boatSelectionPanels
        _SelectionBoatDisplay = new ArrayList<>();
        SelectionBoatDisplay player1_BoatSelection = new SelectionBoatDisplay(GlobalResources.First_Player);
        SelectionBoatDisplay player2_BoatSelection = new SelectionBoatDisplay(GlobalResources.Second_Player);
        _SelectionBoatDisplay.add(player1_BoatSelection);
        _SelectionBoatDisplay.add(player2_BoatSelection);

        this.add(player1_BoatSelection);
        this.add(player2_BoatSelection);
        player1_BoatSelection.setLocation(GlobalResources.ATTRIBUTESFORBOAT_Player1XLocation,GlobalResources.ATTRIBUTESFORBOAT_Player1YLocation);
        player2_BoatSelection.setLocation(GlobalResources.ATTRIBUTESFORBOAT_Player2XLocation,GlobalResources.ATTRIBUTESFORBOAT_Player2YLocation);
        player1_BoatSelection.setVisible(true);
        player2_BoatSelection.setVisible(true);
        player1_BoatSelection.DefaultBoatSelection();
        player2_BoatSelection.DefaultBoatSelection();

        // Keyboard labels
        AssignKeyboard player1_KeyboardLayout = new AssignKeyboard(GlobalResources.First_Player);
        AssignKeyboard player2_KeyboardLayout = new AssignKeyboard(GlobalResources.Second_Player);
        int PlayerOne_KeyboardHelp_Xaxis = GlobalResources.ATTRIBUTESFORBOAT_Player1XLocation + player1_BoatSelection.getWidth() + GlobalResources.ATTRIBUTESFORHELP_SpacingXLocation;
        int PlayerOne_KeyboardHelp_Yaxis = GlobalResources.ATTRIBUTESFORBOAT_Player1YLocation;
        int PlayerTwo_KeyboardHelp_Xaxis = GlobalResources.ATTRIBUTESFORBOAT_Player2XLocation + player2_BoatSelection.getWidth() + GlobalResources.ATTRIBUTESFORHELP_SpacingXLocation;
        int PlayerTwo_KeyboardHelp_Yaxis = GlobalResources.ATTRIBUTESFORBOAT_Player2YLocation;

        player1_KeyboardLayout.setLocation(PlayerOne_KeyboardHelp_Xaxis,PlayerOne_KeyboardHelp_Yaxis);
        player2_KeyboardLayout.setLocation(PlayerTwo_KeyboardHelp_Xaxis,PlayerTwo_KeyboardHelp_Yaxis);
        this.add(player1_KeyboardLayout);
        this.add(player2_KeyboardLayout);

        // Map selection panel
        SelectionMapDisplay selectionMapDisplay = new SelectionMapDisplay();
        int mapSelectionPanel_X = PlayerOne_KeyboardHelp_Xaxis + player1_KeyboardLayout.getWidth() +  GlobalResources.ATTRIBUTESFORMAP_Spacing;
        selectionMapDisplay.setLocation(mapSelectionPanel_X, PlayerOne_KeyboardHelp_Yaxis);

        this.add(selectionMapDisplay);
        _SelectionMapDisplay = selectionMapDisplay;
    }

    // Start game button and return to main menu override events
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // Each starting with new configuration
        
        // Return to menu
        if(e.getSource() == _ButtonMainMenu)
        {
            GlobalResources.MainControl.NavigatingBackToMenuMainDisplay();
        }

        // Start game
        if(e.getSource() == _ButtonStartGame)
        {
            GlobalResources.MainControl.LaunchGame();
        }
    }
}
