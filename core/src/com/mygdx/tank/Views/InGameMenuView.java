    package com.mygdx.tank.Views;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.Screen;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.scenes.scene2d.InputEvent;
    import com.badlogic.gdx.scenes.scene2d.Stage;
    import com.badlogic.gdx.scenes.scene2d.ui.Button;
    import com.badlogic.gdx.scenes.scene2d.ui.Image;
    import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
    import com.badlogic.gdx.scenes.scene2d.ui.Label;
    import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
    import com.badlogic.gdx.scenes.scene2d.ui.Table;
    import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
    import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
    import com.badlogic.gdx.utils.Align;
    import com.esotericsoftware.kryonet.Client;
    import com.esotericsoftware.kryonet.Server;
    import com.mygdx.tank.AccountService;
    import com.mygdx.tank.Constants;
    import com.mygdx.tank.GameView;
    import com.mygdx.tank.MusicManager;
    import com.mygdx.tank.TankMazeMayhem;
    import com.mygdx.tank.controllers.ApplicationController;
    import com.mygdx.tank.model.GameModel;
    import com.mygdx.tank.model.MenuModel;
    import com.mygdx.tank.model.Scoreboard;

    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class InGameMenuView implements Screen {
        private final TankMazeMayhem game;
        private final AccountService accountService;
        private final GameView view;
        private Constants con;
        private Texture background;
        private Stage stage;
        private TextButton exitGameButton;
        private Button closeButton;
        private SpriteBatch batch;
        private Table scoreboardTable;
        private ScrollPane scrollPane;
        private final Scoreboard scoreboard;
        private Client client;
        private Server server;
        private MusicManager musicManager;
        private GameModel gameModel;
        private MenuModel menuModel;


        public InGameMenuView(TankMazeMayhem game, AccountService accountService, Scoreboard scoreboard, GameView view, MenuModel mainMenuModel, Server server) {
            this.game = game;
            this.accountService = accountService;
            this.scoreboard = scoreboard;
            this.view = view;
            this.server = server;
            this.musicManager = game.getMusicManager();
            this.menuModel = mainMenuModel;
        }

        public InGameMenuView(TankMazeMayhem game, AccountService accountService, Scoreboard scoreboard, GameView view, Client client, MenuModel mainMenuModel, GameModel model) {
            this.game = game;
            this.accountService = accountService;
            this.scoreboard = scoreboard;
            this.view = view;
            this.client = client;
            this.menuModel = mainMenuModel;
            this.gameModel = model;
            this.musicManager = game.getMusicManager();
        }

        @Override
        public void show() {
            // Clear the screen
            Gdx.gl.glClearColor(0, 0, 0, 1);

            stage = new Stage();
            batch = new SpriteBatch();
            con = Constants.getInstance();
            background = new Texture("Backgrounds/orange.png");
            exitGameButton = new TextButton("Exit Game", con.getSkin());
            closeButton = new Button(con.getSkin(), "close");

            Image backgroundImage = new Image(background);
            backgroundImage.setBounds(con.getSWidth() * 0.2f, con.getSHeight() * 0.1f, con.getSWidth() * 0.6f, con.getSHeight() * 0.8f);
            stage.addActor(backgroundImage);

            setButtons();
            createHeadline();
            createSoundControl();
            createLeaderboardTable();
            addListeners();

            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);

            // Update and render the stage
            stage.act();
            stage.draw();
        }

        @Override
        public void resize(int width, int height) {

        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void hide() {
            stage.dispose();
            batch.dispose();
        }

        @Override
        public void dispose() {
            stage.dispose();
            batch.dispose();
        }

        private void setButtons() {
            closeButton.setSize(con.getIBSize(), con.getIBSize());
            closeButton.setPosition(con.getSWidth() * 0.73f, con.getSHeight() * 0.78f);
            closeButton.getColor().set(Color.BLACK);

            exitGameButton.setSize(con.getTBWidth(), con.getTBHeight());
            exitGameButton.getLabel().setFontScale(con.getTScaleF());
            exitGameButton.setPosition(con.getCenterTB(), con.getSHeight() * 0.12f);
            exitGameButton.getColor().set(Color.BLACK);

            stage.addActor(closeButton);
            stage.addActor(exitGameButton);
        }

        private void createHeadline() {
            Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
            Label headlineLabel = new Label("Menu settings", headlineStyle);
            headlineLabel.setFontScale(con.getTScaleF() * 1.5f);
            headlineLabel.setAlignment(Align.center);
            headlineLabel.setY((con.getSHeight() * 0.9f) - headlineLabel.getPrefHeight());
            headlineLabel.setWidth(con.getSWidth());
            stage.addActor(headlineLabel);

            Label.LabelStyle scoreHeadLineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK);
            Label scoreHeadLineLabel = new Label("Current scoreboard", scoreHeadLineStyle);
            scoreHeadLineLabel.setFontScale(con.getTScaleF() * 1.2f);
            scoreHeadLineLabel.setAlignment(Align.center);
            scoreHeadLineLabel.setY((con.getSHeight() * 0.69f) - scoreHeadLineLabel.getPrefHeight());
            scoreHeadLineLabel.setWidth(con.getSWidth());
            stage.addActor(scoreHeadLineLabel);
        }

        private void addListeners() {
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    view.toggleMenu();
                }
            });
            exitGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (client != null) {
                        gameModel.playerDisconnected(accountService.getCurrentUser().getPlayer().getPlayerName());
                        client.close();
                    } else if (server != null) {
                        server.close();
                    }
                    ApplicationController.getInstance(game, accountService).switchToMainMenu();
                }
            });
        }

        private void createSoundControl() {
            Label.LabelStyle soundStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK);
            Label soundLabel = new Label("Music", soundStyle);
            soundLabel.setFontScale(con.getTScaleF()* 1.2f);
            soundLabel.setX(con.getSWidth() * 0.4f);
            soundLabel.setY((con.getSHeight()*0.78f) - soundLabel.getPrefHeight());
            stage.addActor(soundLabel);

            ImageButton soundControl = new ImageButton(con.getSkin(), "music");
            soundControl.setSize(con.getIBSize(), con.getIBSize());
            soundControl.getImageCell().expand().fill();
            soundControl.setPosition(con.getSWidth() * 0.6f, (con.getSHeight()*0.75f) - soundLabel.getPrefHeight());
            if(musicManager.isGameMusicPlaying()){
                soundControl.toggle(); //For visual correctness
            }

            soundControl.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    musicManager.muteGameMusic(musicManager.isGameMusicPlaying());
                }
            });
            stage.addActor(soundControl);
        }

        private void createLeaderboardTable() {
            scoreboardTable = new Table();
            scoreboardTable.top(); // Align the items at the top of the table
            scoreboardTable.defaults().expand().fillX();

            float orangeBoxWidth = con.getSWidth() * 0.4f;
            float orangeBoxHeight = con.getSHeight() * 0.35f;
            float orangeBoxStartY = con.getSHeight() * 0.4f;
            float tableStartY = orangeBoxStartY + (orangeBoxHeight / 2);

            scoreboardTable.setSize(orangeBoxWidth, orangeBoxHeight);
            scoreboardTable.setPosition((con.getSWidth() - orangeBoxWidth) / 2, tableStartY);

            // Create a scroll pane for the leaderboardTable
            scrollPane = new ScrollPane(scoreboardTable, con.getSkin());
            scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
            scrollPane.setPosition(scoreboardTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

            stage.addActor(scrollPane);

            handleLeaderboardData(scoreboard.getScoreboard());
        }

        private void handleLeaderboardData(HashMap<String, Integer> entries) {
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(entries.entrySet());

            // Sort the list based on the values
            Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    // Compare the values in descending order
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });

            // Populate the scoreboard table with sorted entries
            populateLeaderBoardTable(sortedEntries);
        }

        private void populateLeaderBoardTable(List<Map.Entry<String, Integer>> scoreboard) {
            scoreboardTable.clearChildren();
            float columnWidth = scrollPane.getWidth() / 2f - 10f;
            for (Map.Entry<String, Integer> entry : scoreboard) {
                String userName = entry.getKey();
                Integer score = entry.getValue();
                Label nameLabel = new Label(userName, new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));
                Label scoreLabel = new Label(String.valueOf(score), new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));

                nameLabel.setFontScale(con.getTScaleF());
                scoreLabel.setFontScale(con.getTScaleF());

                scoreboardTable.row().pad(10f).fillX();
                scoreboardTable.add(nameLabel).width(columnWidth);
                scoreboardTable.add(scoreLabel).width(columnWidth);
            }
            scoreboardTable.pack();
        }
    }
