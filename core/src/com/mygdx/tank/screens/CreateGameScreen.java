package com.mygdx.tank.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.tank.AccountService;
import com.mygdx.tank.Constants;
import com.mygdx.tank.FirebaseInterface;
import com.mygdx.tank.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.User;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.tank.*;
import com.mygdx.tank.model.components.powerup.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateGameScreen implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final Constants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final Skin skin;
    private final TextButton backButton, startGameButton;
    private Server server;
    private Client client;
    private final LinkedHashMap<Integer, Player> connectedPlayers = new LinkedHashMap<>();
    private Table playersTable;
    private ScrollPane scrollPane;
    private User user;
    private Listener listener;

    public CreateGameScreen(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");
        skin = new Skin(Gdx.files.internal("skins/orange/skin/uiskin.json"));

        backButton = new TextButton("Back", skin, "default");
        startGameButton = new TextButton("Start game", skin,"default");
    }

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();
        server = new Server();
        server.start();

        try {
            server.bind(54555);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.getKryo().register(String.class);
        server.getKryo().register(ArrayList.class);
        server.getKryo().register(HashMap.class);
        server.getKryo().register(PositionComponent.class);
        server.getKryo().register(SpriteDirectionComponent.class);
        server.getKryo().register(Float.class);
        server.getKryo().register(Player.class);
        server.getKryo().register(PowerUpTypeComponent.class);
        server.getKryo().register(PowerUpTypeComponent.PowerupType.class);
        server.getKryo().register(PlayerScoreComponent.class);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    server.sendToAllTCP(message);
                } else if (object instanceof List) {
                    List<Object> list = (List<Object>) object;
                    server.sendToAllExceptTCP(connection.getID(), list);
                } else if (object instanceof Player) {
                    if (connectedPlayers.size() != 0) {
                        Player lastPlayer = (Player) connectedPlayers.values().toArray()[connectedPlayers.size() - 1];
                        String lastPlayerName = lastPlayer.getPlayerName();
                        int lastPlayerNumber = Integer.parseInt(lastPlayerName.substring(lastPlayerName.length() -1 ));
                        String newUsername = "Player" + (lastPlayerNumber + 1);
                        Player player = (Player) object;
                        player.setPlayerName(newUsername);
                        connectedPlayers.put(connection.getID(), player);
                        List<Player> connectedPlayersList = new ArrayList<>(connectedPlayers.values());
                        populatePlayerTable(connectedPlayersList);
                        server.sendToAllTCP(connectedPlayersList);
                    }
                }
            }
        });

        server.addListener(new Listener() {
            @Override
            public void disconnected(Connection connection) {
                int lastConnection = (int) connectedPlayers.keySet().toArray()[connectedPlayers.size() - 1];
                if (lastConnection != connection.getID()) {
                    List<Map.Entry<Integer, Player>> entryList = new ArrayList<>(connectedPlayers.entrySet());
                    int index = 0;
                    for (int i = 0; i < entryList.size(); i++) {
                        Map.Entry<Integer, Player> entry = entryList.get(i);
                        if (entry.getKey() == connection.getID()) {
                            index = i;
                            break;
                        }
                    }
                    for (int i = 0; i < entryList.size(); i++) {
                        if (i > index) {
                            Map.Entry<Integer, Player> entry = entryList.get(i);

                            Player player = entry.getValue();
                            String oldPlayerName = player.getPlayerName();
                            int lastPlayerNumber = Integer.parseInt(oldPlayerName.substring(oldPlayerName.length() -1 ));
                            String newPlayerName = "Player" + (lastPlayerNumber - 1);
                            player.setPlayerName(newPlayerName);
                            server.sendToTCP(entry.getKey(), player);
                        }
                    }
                }
                connectedPlayers.remove(connection.getID());

                List<Player> connectedPlayersList = new ArrayList<>(connectedPlayers.values());
                populatePlayerTable(connectedPlayersList);
                server.sendToAllTCP(connectedPlayersList);
            }
        });

        client = new Client();
        client.start();

        client.getKryo().register(String.class);
        client.getKryo().register(ArrayList.class);
        client.getKryo().register(HashMap.class);
        client.getKryo().register(PositionComponent.class);
        client.getKryo().register(SpriteDirectionComponent.class);
        client.getKryo().register(Float.class);
        client.getKryo().register(Player.class);
        client.getKryo().register(PowerUpTypeComponent.class);
        client.getKryo().register(PowerUpTypeComponent.PowerupType.class);
        client.getKryo().register(PlayerScoreComponent.class);

        listener = new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof String) {
                    String message = (String) object;
                    System.out.println("Klient mottok denne meldingen fra server: " + message);
                    System.out.println("Meldingen var fra: " + connection.getID());
                }
            }
        };

        client.addListener(listener);
        try {
            InetAddress IP = InetAddress.getLocalHost();
            client.connect(5000, IP.getHostAddress(), 54555);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        user = accountService.getCurrentUser();
        Player player = new Player("Player1", user.getUserMail());
        connectedPlayers.put(1, player);
        user.setPlayer(player);

        setButtons();
        createHeadline();
        createPlayersTable();
        addListeners();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, con.getSWidth(), con.getSHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        batch.dispose();
        skin.dispose();
        try {
            server.dispose();
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setButtons() {
        backButton.setBounds(con.getCenterTB() - con.getTBWidth() / 2 - con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        backButton.getLabel().setFontScale(con.getTScaleF());

        startGameButton.setBounds(con.getCenterTB() + con.getTBWidth() / 2 + con.getTBWidth() / 5, con.getSHeight()*0.05f, con.getTBWidth(), con.getTBHeight());
        startGameButton.getLabel().setFontScale(con.getTScaleF());

        stage.addActor(backButton);
        stage.addActor(startGameButton);
    }

    private void addListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LobbyScreen(game,firebaseInterface, accountService));
                server.close();
            }
        });
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                client.removeListener(listener);
                client.sendTCP("GameStart");
                List<Player> connectedPlayersList = new ArrayList<>(connectedPlayers.values());
                game.setScreen(new InGameScreen(game, accountService, client, connectedPlayersList, server));
            }
        });
    }

    private void createHeadline() {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(skin.getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Party", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF());
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());
        stage.addActor(headlineLabel);
    }

    private void createPlayersTable() {
        playersTable = new Table();
        playersTable.top(); // Align the items at the top of the table
        playersTable.defaults().expand().fillX();

        float orangeBoxWidth = con.getSWidth() * 0.5f;
        float orangeBoxHeight = con.getSHeight() * 0.5f;
        float orangeBoxStartY = con.getSHeight() * 0.27f;
        float tableStartY = orangeBoxStartY + (orangeBoxHeight / 2);

        playersTable.setSize(orangeBoxWidth, orangeBoxHeight);
        playersTable.setPosition((con.getSWidth() - orangeBoxWidth) / 2, tableStartY);

        // Create a scroll pane for the leaderboardTable
        scrollPane = new ScrollPane(playersTable, skin);
        scrollPane.setSize(orangeBoxWidth, orangeBoxHeight);
        scrollPane.setPosition(playersTable.getX(), con.getSHeight() - orangeBoxStartY - orangeBoxHeight);

        stage.addActor(scrollPane);
        List<Player> connectedPlayersList = new ArrayList<>(connectedPlayers.values());
        populatePlayerTable(connectedPlayersList);
    }

    private void populatePlayerTable(List<Player> players) {
        playersTable.clearChildren();
        float columnWidth = scrollPane.getWidth() / 2f - 10f;
        for (Player player : players) {
            String userMail = player.getUserMail();
            String displayName = userMail.split("@")[0];
            Label nameLabel = new Label(displayName, new Label.LabelStyle(skin.getFont("font"), Color.BLACK));
            Label scoreLabel = new Label("Connected", new Label.LabelStyle(skin.getFont("font"), Color.BLACK));

            nameLabel.setFontScale(con.getTScaleF());
            scoreLabel.setFontScale(con.getTScaleF());

            playersTable.row().pad(10f).fillX();
            playersTable.add(nameLabel).width(columnWidth);
            playersTable.add(scoreLabel).width(columnWidth);
        }
        playersTable.pack();
    }
}

