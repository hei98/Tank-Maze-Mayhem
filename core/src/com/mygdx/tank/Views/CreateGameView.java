package com.mygdx.tank.Views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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
import com.mygdx.tank.model.Player;
import com.mygdx.tank.TankMazeMayhem;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.tank.model.User;
import com.mygdx.tank.controllers.ApplicationController;
import com.mygdx.tank.model.MenuModel;
import com.mygdx.tank.model.components.*;
import com.mygdx.tank.model.components.tank.*;
import com.mygdx.tank.model.components.powerup.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.net.NetworkInterface;

public class CreateGameView implements Screen {
    private final FirebaseInterface firebaseInterface;
    private final Constants con;
    private final TankMazeMayhem game;
    private final AccountService accountService;
    private final Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private final TextButton backButton, startGameButton;
    private Server server;
    private Client client;
    private final LinkedHashMap<Integer, Player> connectedPlayers = new LinkedHashMap<>();
    private Table playersTable;
    private ScrollPane scrollPane;
    private User user;
    private Listener listener;
    private Listener serverListener;
    private MenuModel menuModel;

    public CreateGameView(TankMazeMayhem game, FirebaseInterface firebaseInterface, AccountService accountService, MenuModel menuModel) {
        this.game = game;
        this.firebaseInterface = firebaseInterface;
        this.accountService = accountService;
        this.menuModel = menuModel;
        con = Constants.getInstance();
        background = new Texture("Backgrounds/Leaderboard.png");

        backButton = new TextButton("Back", con.getSkin(), "default");
        startGameButton = new TextButton("Start game", con.getSkin(),"default");
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
                    if (!connectedPlayers.isEmpty()) {
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

        serverListener = new Listener() {
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
        };

        server.addListener(serverListener);

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
        try {
            createHeadline();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
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
                ApplicationController.getInstance(game, accountService).switchToLobby();
                server.close();
            }
        });
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                client.removeListener(listener);
                server.removeListener(serverListener);
                client.sendTCP("GameStart");
                List<Player> connectedPlayersList = new ArrayList<>(connectedPlayers.values());
                game.setScreen(new InGameView(game, accountService, client, connectedPlayersList, server, menuModel));
            }
        });
    }

    private void createHeadline() throws UnknownHostException {
        Label.LabelStyle headlineStyle = new Label.LabelStyle(con.getSkin().getFont("font"), Color.WHITE);
        Label headlineLabel = new Label("Party", headlineStyle);
        headlineLabel.setFontScale(con.getTScaleF());
        headlineLabel.setAlignment(Align.center);
        headlineLabel.setY((con.getSHeight()*0.8f) - headlineLabel.getPrefHeight());
        headlineLabel.setWidth(con.getSWidth());

        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        String ip = getIpAddress();

        Label label;
        if (ip.equals("10.0.2.15") || ip.equals("10.0.2.16")) {
            label = new Label("Your IP-address is: 10.0.2.2, port 5000", labelStyle);
        } else {
            label = new Label("Your IP-address is: " + ip + ", port 54555", labelStyle);
        }

        label.setAlignment(Align.center);
        label.setY(con.getSHeight() * 0.2f);
        label.setFontScale(con.getTScaleF());
        label.setWidth(con.getSWidth());

        stage.addActor(label);
        stage.addActor(headlineLabel);
    }

    public String getIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    final String ip = addr.getHostAddress();
                    if (Inet4Address.class == addr.getClass()) return ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return null;
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
        scrollPane = new ScrollPane(playersTable, con.getSkin());
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
            Label nameLabel = new Label(displayName, new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));
            Label scoreLabel = new Label("Connected", new Label.LabelStyle(con.getSkin().getFont("font"), Color.BLACK));

            nameLabel.setFontScale(con.getTScaleF());
            scoreLabel.setFontScale(con.getTScaleF());

            playersTable.row().pad(10f).fillX();
            playersTable.add(nameLabel).width(columnWidth);
            playersTable.add(scoreLabel).width(columnWidth);
        }
        playersTable.pack();
    }
}

