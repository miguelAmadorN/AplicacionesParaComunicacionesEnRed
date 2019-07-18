
import afines.Coordenada;
import afines.MensajeFlujo;
import afines.UIFunctions;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import static servidores.Mediador.MOVIMIENTO;
import static servidores.Mediador.SALIR;
import servidores.Vista;

class GPanel extends JPanel implements MouseListener {

    OthelloBoard board;
    ImageIcon button_black, button_white;
    JLabel score_black, score_white, turn;
    String gameTheme;
    Move hint = null;
    boolean inputEnabled, active;

    private Operaciones operaciones = null;
    private String nickname;
    private String nicknameOp;

    private final int WINNER = 1;
    private final int LOSER = 2;
    private final int DRAW = 3;
    private final int PASS = 4;
    private final int CONTINUE = 4;

    public GPanel(OthelloBoard board, JLabel score_black, JLabel score_white, JLabel turn,
            String theme, String nickname, Operaciones operaciones) throws IOException, SocketException, ClassNotFoundException {
        super();
        this.board = board;
        this.score_black = score_black;
        this.score_white = score_white;
        this.turn = turn;
        setTheme(theme);
        addMouseListener(this);
        this.nickname = nickname;
        this.operaciones = operaciones;
        inputEnabled = operaciones.isIniciar();
        showTurn(inputEnabled);
        nicknameOp = operaciones.getNickOponente();
        active = true;
        acciones();//-> hilo socket

    }
    
    private void showTurn(boolean isTurn)
    {
        if(isTurn)
        {
            turn.setText("Your turn");
        }
        else
        {
           turn.setText("wait..."); 
        }
    }
    
    public void setTheme(String gameTheme) {
        hint = null;
        this.gameTheme = gameTheme;
        if (gameTheme.equals("Classic")) {
            button_black = new ImageIcon(Othello.class.getResource("button_black.jpg"));
            button_white = new ImageIcon(Othello.class.getResource("button_white.jpg"));
            setBackground(Color.green);
        } else if (gameTheme.equals("Electric")) {
            button_black = new ImageIcon(Othello.class.getResource("button_blu.jpg"));
            button_white = new ImageIcon(Othello.class.getResource("button_red.jpg"));
            setBackground(Color.white);
        } else {
            gameTheme = "Flat"; // default theme "Flat"
            setBackground(Color.green);
        }
        repaint();
    }

    public void drawPanel(Graphics g) {
//	    int currentWidth = getWidth();
//		int currentHeight = getHeight();
        for (int i = 1; i < 8; i++) {
            g.drawLine(i * Othello.Square_L, 0, i * Othello.Square_L, Othello.Height);
        }
        g.drawLine(Othello.Width, 0, Othello.Width, Othello.Height);
        for (int i = 1; i < 8; i++) {
            g.drawLine(0, i * Othello.Square_L, Othello.Width, i * Othello.Square_L);
        }
        g.drawLine(0, Othello.Height, Othello.Width, Othello.Height);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (board.get(i, j)) {
                    case white:
                        if (gameTheme.equals("Flat")) {
                            g.setColor(Color.white);
                            g.fillOval(1 + i * Othello.Square_L, 1 + j * Othello.Square_L, Othello.Square_L - 1, Othello.Square_L - 1);
                        } else {
                            g.drawImage(button_white.getImage(), 1 + i * Othello.Square_L, 1 + j * Othello.Square_L, this);
                        }
                        break;
                    case black:
                        if (gameTheme.equals("Flat")) {
                            g.setColor(Color.black);
                            g.fillOval(1 + i * Othello.Square_L, 1 + j * Othello.Square_L, Othello.Square_L - 1, Othello.Square_L - 1);
                        } else {
                            g.drawImage(button_black.getImage(), 1 + i * Othello.Square_L, 1 + j * Othello.Square_L, this);
                        }
                        break;
                }
            }
        }
        if (hint != null) {
            g.setColor(Color.darkGray);
            g.drawOval(hint.i * Othello.Square_L + 3, hint.j * Othello.Square_L + 3, Othello.Square_L - 6, Othello.Square_L - 6);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPanel(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(Othello.Width, Othello.Height);
    }

    public int getResult() {
        if (board.counter[0] > board.counter[1]) {
            return WINNER;
        } else if (board.counter[0] < board.counter[1]) {
            return LOSER;
        } else {
            return DRAW;
        }
    }

    public void showWinner() {
        inputEnabled = false;
        active = false;
        if (board.counter[0] > board.counter[1]) {
            JOptionPane.showMessageDialog(this, "You win!", "Othello", JOptionPane.INFORMATION_MESSAGE);
        } else if (board.counter[0] < board.counter[1]) {
            JOptionPane.showMessageDialog(this, nicknameOp + " win!", "Othello", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Drawn!", "Othello", JOptionPane.INFORMATION_MESSAGE);
        }
        showTurn(inputEnabled);
    }

    public void setHint(Move hint) {
        this.hint = hint;
    }

    public void clear(boolean black) {
        board.clear(black);
        score_black.setText(nickname + ": " +Integer.toString(board.getCounter(TKind.black)));
        score_white.setText(nicknameOp + ": " +Integer.toString(board.getCounter(TKind.white)));
        inputEnabled = operaciones.isIniciar();
        active = true;
        showTurn(inputEnabled);
    }

    //userCanMove -> se puede mover en esa coordenada
    //move poner los valores de x,y colocar coordenada
    //inputEnabled -> habilitacion del mouse listener
    //panel.clear -> inicializar ahí los valores de quién inicia
    /////////
    public void computerMove() throws IOException, ClassNotFoundException {//AI
        if (board.gameEnd()) 
        {
            showWinner();
            return;
        }
        if (board.userCanMove(TKind.white)) 
        {

            
        } else if (board.gameEnd()) {
            showWinner();

        } else {
            JOptionPane.showMessageDialog(this, nicknameOp +" pass...", "Othello", JOptionPane.INFORMATION_MESSAGE);
            inputEnabled = true;
            showTurn(inputEnabled);
            //----->//enviar mensaje de moviento 
        }
    }

    public void mouseClicked(MouseEvent e) {
// generato quando il mouse viene premuto e subito rilasciato (click)

        if (inputEnabled) 
        {
            hint = null;
            int i = e.getX() / Othello.Square_L;
            int j = e.getY() / Othello.Square_L;
            if ((i < 8) && (j < 8) && (board.get(i, j) == TKind.nil) && (board.move(new Move(i, j), TKind.black) != 0)) {

                try {
                    //----->//enviar mensaje de moviento
                    operaciones.enviarMovimiento(new Coordenada(i, j), true);
                    inputEnabled = false;
                    showTurn(inputEnabled);
                } catch (IOException ex) {
                    Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

                score_black.setText(nickname + ": " +Integer.toString(board.getCounter(TKind.black)));
                score_white.setText(nicknameOp + ": " +Integer.toString(board.getCounter(TKind.white)));
                repaint();
                try {
                    //Modificado
                    computerMove();
                    //Modificado
                } catch (IOException ex) {
                    Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Illegal move", "Othello", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    private void acciones()
    {
        Thread hilo = new Thread()
        {
            public void run()
            {
                while (true) 
                {
                    if (operaciones != null) {
                        MensajeFlujo m;
                        try {
                            m = operaciones.esperarMensaje();  
                            if (m.getIdMensaje() == MOVIMIENTO) 
                            {

                                Move move = new Move(m.getCoordenada().getX(), m.getCoordenada().getY());
                                board.move(move, TKind.white);
                                score_black.setText(nickname + ": " +Integer.toString(board.getCounter(TKind.black)));
                                score_white.setText(nicknameOp + ": " +Integer.toString(board.getCounter(TKind.white)));
                                repaint();
                                inputEnabled = true;
                                showTurn(inputEnabled);
                                if (board.gameEnd()) {
                                    showWinner();
                                } else if (!board.userCanMove(TKind.black)) {
                                    inputEnabled = false;
                                    showTurn(inputEnabled);
                                    showMessage("You pass..", "Othello");

                                    try {
                                        computerMove();
                                    } catch (IOException ex) {
                                        Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                            }
                            else if(m.getIdMensaje() == SALIR)
                            {
                                operaciones.clear();
                                int input = JOptionPane.showConfirmDialog(null, nicknameOp + " se desconecto."
                                        + " Deseas buscar otro oponente?");
                                inputEnabled = false;
                                showTurn(inputEnabled);
                                if(input == 0)
                                {
                                    if (!operaciones.conectar(nickname)) {
                                        UIFunctions.informationMessage("No hay servidor disponibles", "");
                                        System.exit(0);
                                    }
                                    nicknameOp = operaciones.getNickOponente();
                                    board.clear(operaciones.isIniciar());
                                    score_black.setText(nickname + ": " +Integer.toString(board.getCounter(TKind.black)));
                                    score_white.setText(nicknameOp + ": " +Integer.toString(board.getCounter(TKind.white)));
                                    inputEnabled = operaciones.isIniciar();
                                    showTurn(inputEnabled);
                                    active = true;
                                    repaint();

                                }
                                else if(input == 1)
                                    System.exit(0);

                            }

                        } catch (IOException ex) {
                            Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(GPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        };
        hilo.start();
        
    }
    
    private void showMessage(String mensaje, String ventanaNombre)
    {
        JOptionPane.showMessageDialog(this, mensaje, ventanaNombre,
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    public void mouseEntered(MouseEvent e) {
// generato quando il mouse entra nella finestra
    }

    public void mouseExited(MouseEvent e) {
// generato quando il mouse esce dalla finestra
    }

    public void mousePressed(MouseEvent e) {
// generato nell'istante in cui il mouse viene premuto
    }

    public void mouseReleased(MouseEvent e) {
// generato quando il mouse viene rilasciato, anche a seguito di click
    }

};

public class Othello extends JFrame implements ActionListener {

    JEditorPane editorPane;

    static final String WindowTitle = "Othello";
    static final String ABOUTMSG = WindowTitle + "\n\n26-12-2006\njavalc6";

    static GPanel gpanel;
    static JMenuItem hint;
    static boolean helpActive = false;

    static final int Square_L = 33; // length in pixel of a square in the grid
    static final int Width = 8 * Square_L; // Width of the game board
    static final int Height = 8 * Square_L; // Width of the game board

    OthelloBoard board;
    static JLabel score_black, score_white, turn;
    JMenu theme;

    private String nickname = "";
    private String nicknameOponente = "";
    private Operaciones operaciones;

    public Othello() throws IOException, SocketException, ClassNotFoundException {
        super(WindowTitle);
        DialogoNickname dn = new DialogoNickname(new javax.swing.JFrame(), true);
        dn.setVisible(true);
        nickname = dn.getNickname();
        if (nickname.length() < 1) {
            System.exit(0);
        }

        System.out.print("Esperando oponente...\n");
        operaciones = new Operaciones();

        if (!operaciones.conectar(nickname)) 
        {
            UIFunctions.informationMessage("No hay servidor disponibles", "");
            System.exit(0);
        }
        
        nicknameOponente = operaciones.getNickOponente();
        score_black = new JLabel(nickname + ": 2"); // the game start with 2 black pieces
        score_black.setForeground(Color.blue);
        score_black.setFont(new Font("Dialog", Font.BOLD, 16));
        score_white = new JLabel(nicknameOponente + ": 2"); // the game start with 2 white pieces
        score_white.setForeground(Color.red);
        score_white.setFont(new Font("Dialog", Font.BOLD, 16));
        
        turn = new JLabel();
        turn.setFont(new Font("Dialog", Font.BOLD, 14));
        
        board = new OthelloBoard(operaciones.isIniciar());
        gpanel = new GPanel(board, score_black, score_white, turn,"Electric", nickname, operaciones);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setupMenuBar();
        gpanel.setMinimumSize(new Dimension(Othello.Width, Othello.Height));

        JPanel status = new JPanel();
        status.setLayout(new BorderLayout());
        status.add(score_black, BorderLayout.WEST);
        status.add(score_white, BorderLayout.EAST);
        status.add(turn,  BorderLayout.CENTER);
//		status.setMinimumSize(new Dimension(100, 30));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gpanel, status);
        splitPane.setOneTouchExpandable(false);
        getContentPane().add(splitPane);
        this.setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setResizable(false);
        
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                try {
                    close();
                } catch (IOException ex) {
                    Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            }
        });
    }

// voci del menu di primo livello
// File Edit Help
    
    private void close() throws IOException{
        if (JOptionPane.showConfirmDialog(rootPane, "¿Deseas salir?",
                "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            if(operaciones != null)
            {
                operaciones.enviarMensajeSalir();
            }
            System.exit(0);
        }
    }
//
    void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildGameMenu());
        menuBar.add(buildHelpMenu());
        setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        String action = source.getText();
        if (action.equals("Classic")) {
            gpanel.setTheme(action);
        } else if (action.equals("Electric")) {
            gpanel.setTheme(action);
        } else if (action.equals("Flat")) {
            gpanel.setTheme(action);
        }
    }

    protected JMenu buildGameMenu() {
        JMenu game = new JMenu("Game");
        theme = new JMenu("Theme");
        JMenuItem undo = new JMenuItem("Undo");
        hint = new JMenuItem("Hint");
        undo.setEnabled(false);

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("2");

// build theme sub-menu
        group = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("Classic");
        group.add(rbMenuItem);
        theme.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("Electric", true);
        group.add(rbMenuItem);
        theme.add(rbMenuItem);
        rbMenuItem.addActionListener(this);
        rbMenuItem = new JRadioButtonMenuItem("Flat");
        group.add(rbMenuItem);
        theme.add(rbMenuItem);
        rbMenuItem.addActionListener(this);

// Begin "New"
/*
		newWin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gpanel.clear();
				hint.setEnabled(true);
				repaint();
			}});
         */
// End "New"


// Begin "Hint"
        hint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (gpanel.active) {
                    Move move = new Move();
                    if (board.findMove(TKind.black, 3, move)) {
                        gpanel.setHint(move);
                    }
                    repaint();

                } else {
                    hint.setEnabled(false);
                }
            }
        });
// End "Hint"

        //game.add(newWin);
        game.addSeparator();
        game.add(undo);
        game.add(hint);
        game.addSeparator();
        game.add(theme);
        game.addSeparator();
        return game;
    }

    protected JMenu buildHelpMenu() {
        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About " + WindowTitle + "...");
        JMenuItem openHelp = new JMenuItem("Help Topics...");

// Begin "Help"
        openHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createEditorPane();
            }
        });
// End "Help"

// Begin "About"
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = new ImageIcon(Othello.class.getResource("reversi.jpg"));
                JOptionPane.showMessageDialog(null, ABOUTMSG, "About " + WindowTitle, JOptionPane.PLAIN_MESSAGE, icon);
            }
        });
// End "About"

        help.add(openHelp);
        help.add(about);

        return help;
    }

    protected void createEditorPane() {
        if (helpActive) {
            return;
        }
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument) editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
                                (HTMLFrameHyperlinkEvent) e);
                    } else {
                        try {
                            editorPane.setPage(e.getURL());
                        } catch (java.io.IOException ioe) {
                            System.out.println("IOE: " + ioe);
                        }
                    }
                }
            }
        });
        java.net.URL helpURL = Othello.class.getResource("HelpFile.html");
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
                new HelpWindow(editorPane);
            } catch (java.io.IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        } else {
            System.err.println("Couldn't find file: HelpFile.html");
        }

        return;
    }

    public class HelpWindow extends JFrame {

        public HelpWindow(JEditorPane editorPane) {
            super("Help Window");
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Othello.helpActive = false;
                    setVisible(false);
                }
            });

            JScrollPane editorScrollPane = new JScrollPane(editorPane);
            editorScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            getContentPane().add(editorScrollPane);
            setSize(600, 400);
            setVisible(true);
            helpActive = true;
        }
    }

    public HyperlinkListener createHyperLinkListener1() {
        return new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        ((HTMLDocument) editorPane.getDocument()).processHTMLFrameHyperlinkEvent(
                                (HTMLFrameHyperlinkEvent) e);
                    } else {
                        try {
                            editorPane.setPage(e.getURL());
                        } catch (java.io.IOException ioe) {
                            System.out.println("IOE: " + ioe);
                        }
                    }
                }
            }
        };
    }

    public static void main(String[] args) throws IOException, SocketException, ClassNotFoundException {

        Othello app = new Othello();
    }

}
