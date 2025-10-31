package javaGuiEventListenersDemo;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.*;

/**
 * Complete Java Swing Event Handlers Learning Application
 *
 * This application demonstrates ALL major event listeners in Java Swing/AWT.
 * Each event handler is documented with comments explaining. - What triggers
 * the event - When to use it - Common use cases
 *
 * Total Event Handlers Demonstrated: 25+ interfaces with 70+ methods
 */
public class CompleteEventHandlersApp extends JFrame {

    private JTextArea eventLog;
    private int eventCounter = 0;

    public CompleteEventHandlersApp() {
        super("Complete Java Swing Event Handlers Learning App");
        setupUI();
        setupAllEventHandlers();
    }

 private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setPreferredSize(new Dimension(1200, 800)); // preferred size
        setLayout(new BorderLayout());

        // Event Log Area
        eventLog = new JTextArea(15, 50);
        eventLog.setEditable(false);

        float scale = Toolkit.getDefaultToolkit().getScreenResolution() / 96f;
        eventLog.setFont(new Font("Monospaced", Font.PLAIN, (int)(11 * scale))); // scaled font

        JScrollPane logScroll = new JScrollPane(eventLog);
        logScroll.setBorder(BorderFactory.createTitledBorder("Event Log - Watch Events Fire Here!"));
        add(logScroll, BorderLayout.SOUTH);

        // Main Components Panel
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Basic Event Handlers
        tabbedPane.addTab("Basic Events", createBasicEventsPanel());

        // Tab 2: Mouse & Key Events
        tabbedPane.addTab("Mouse & Key", createMouseKeyPanel());

        // Tab 3: Component Events
        tabbedPane.addTab("Component Events", createComponentEventsPanel());

        // Tab 4: Window Events
        tabbedPane.addTab("Window Events", createWindowEventsPanel());

        // Tab 5: Advanced Events
        tabbedPane.addTab("Advanced Events", createAdvancedEventsPanel());

        // Tab 6: Document & Text Events
        tabbedPane.addTab("Text Events", createTextEventsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Instructions Panel
        JLabel instructions = new JLabel("<html><b>Instructions:</b> Interact with components to see events fire. "
                + "Each event shows the listener type, method name, and relevant data.</html>");
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(instructions, BorderLayout.NORTH);

        pack(); // respect preferred size of components
    }

    private JPanel createBasicEventsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. ActionListener - Most common event handler
        JButton actionBtn = new JButton("ActionListener Demo");
        actionBtn.setToolTipText("Fires when button is clicked, menu item selected, or Enter pressed in text field");
        actionBtn.addActionListener(e -> logEvent("ActionListener", "actionPerformed",
                "Button clicked - Command: " + e.getActionCommand()));
        panel.add(actionBtn);

        // 2. ItemListener - For checkboxes, radio buttons, comboboxes
        JCheckBox checkBox = new JCheckBox("ItemListener Demo");
        checkBox.setToolTipText("Fires when checkbox/radio button state changes");
        checkBox.addItemListener(e -> logEvent("ItemListener", "itemStateChanged",
                "State: " + (e.getStateChange() == ItemEvent.SELECTED ? "SELECTED" : "DESELECTED")));
        panel.add(checkBox);

        // 3. ChangeListener - For sliders, progress bars, spinners
        JSlider slider = new JSlider(0, 100, 50);
        slider.setToolTipText("Fires when slider value changes");
        slider.addChangeListener(e -> logEvent("ChangeListener", "stateChanged",
                "Slider value: " + slider.getValue()));
        panel.add(slider);

        // 4. ListSelectionListener - For JList selections
        String[] listData = {"Item 1", "Item 2", "Item 3", "Item 4"};
        JList<String> list = new JList<>(listData);
        list.setToolTipText("Fires when list selection changes");
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                logEvent("ListSelectionListener", "valueChanged",
                        "Selected: " + list.getSelectedValue() + " (Index: " + list.getSelectedIndex() + ")");
            }
        });
        panel.add(new JScrollPane(list));

        // 5. SpinnerListener via ChangeListener
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        spinner.setToolTipText("Spinner uses ChangeListener - fires when value changes");
        spinner.addChangeListener(e -> logEvent("ChangeListener (Spinner)", "stateChanged",
                "Spinner value: " + spinner.getValue()));
        panel.add(spinner);

        // 6. ButtonModel ChangeListener - Advanced button state tracking
        JToggleButton toggleBtn = new JToggleButton("Toggle Button");
        toggleBtn.setToolTipText("Uses ButtonModel ChangeListener - tracks button state changes");
        toggleBtn.getModel().addChangeListener(e -> logEvent("ButtonModel ChangeListener", "stateChanged",
                "Toggle state: " + toggleBtn.isSelected()));
        panel.add(toggleBtn);

        // 7. ComboBox with ItemListener
        JComboBox<String> combo = new JComboBox<>(new String[]{"Option A", "Option B", "Option C"});
        combo.setToolTipText("ComboBox ItemListener - fires when selection changes");
        combo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                logEvent("ItemListener (ComboBox)", "itemStateChanged",
                        "Selected: " + e.getItem());
            }
        });
        panel.add(combo);

        // Clear button
        JButton clearBtn = new JButton("Clear Event Log");
        clearBtn.addActionListener(e -> {
            eventLog.setText("");
            eventCounter = 0;
        });
        panel.add(clearBtn);

        return panel;
    }

    private JPanel createMouseKeyPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Mouse Events Area
        JPanel mouseArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawString("Mouse Event Area - Move, click, drag here!", 10, 20);
            }
        };
        mouseArea.setPreferredSize(new Dimension(400, 200));
        mouseArea.setBorder(BorderFactory.createTitledBorder("Mouse Events Testing Area"));

        // 8. MouseListener - All mouse button events
        mouseArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logEvent("MouseListener", "mouseClicked",
                        "Button: " + e.getButton() + " at (" + e.getX() + "," + e.getY() + ")");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                logEvent("MouseListener", "mousePressed",
                        "Button: " + e.getButton() + " at (" + e.getX() + "," + e.getY() + ")");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                logEvent("MouseListener", "mouseReleased",
                        "Button: " + e.getButton() + " at (" + e.getX() + "," + e.getY() + ")");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                logEvent("MouseListener", "mouseEntered", "Mouse entered component");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logEvent("MouseListener", "mouseExited", "Mouse left component");
            }
        });

        // 9. MouseMotionListener - Mouse movement tracking
        mouseArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                logEvent("MouseMotionListener", "mouseDragged",
                        "Dragging at (" + e.getX() + "," + e.getY() + ")");
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Commented out to avoid spam, but you can enable for learning
                // logEvent("MouseMotionListener", "mouseMoved", 
                //     "Moving at (" + e.getX() + "," + e.getY() + ")");
            }
        });

        // 10. MouseWheelListener - Mouse wheel events
        mouseArea.addMouseWheelListener(e -> logEvent("MouseWheelListener", "mouseWheelMoved",
                "Wheel rotation: " + e.getWheelRotation() + " (Scroll type: "
                + (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL ? "UNIT" : "BLOCK") + ")"));

        panel.add(mouseArea, BorderLayout.CENTER);

        // Key Events Area
        JPanel keyPanel = new JPanel(new FlowLayout());

        JTextField keyField = new JTextField("Type here for key events", 20);
        keyField.setToolTipText("KeyListener - fires for key press, release, and typed events");

        // 11. KeyListener - All keyboard events
        keyField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                logEvent("KeyListener", "keyPressed",
                        "Key: " + KeyEvent.getKeyText(e.getKeyCode()) + " (Code: " + e.getKeyCode() + ")");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                logEvent("KeyListener", "keyReleased",
                        "Key: " + KeyEvent.getKeyText(e.getKeyCode()) + " (Code: " + e.getKeyCode() + ")");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                logEvent("KeyListener", "keyTyped",
                        "Character: '" + e.getKeyChar() + "'");
            }
        });

        keyPanel.add(new JLabel("Key Events:"));
        keyPanel.add(keyField);
        panel.add(keyPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createComponentEventsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 12. FocusListener - Component focus events
        JTextField focusField = new JTextField("Click to focus/unfocus");
        focusField.setToolTipText("FocusListener - fires when component gains/loses focus");
        focusField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                logEvent("FocusListener", "focusGained",
                        "Component gained focus: " + e.getComponent().getClass().getSimpleName());
            }

            @Override
            public void focusLost(FocusEvent e) {
                logEvent("FocusListener", "focusLost",
                        "Component lost focus: " + e.getComponent().getClass().getSimpleName());
            }
        });
        panel.add(focusField);

        // 13. ComponentListener - Component size/position changes
        JPanel resizablePanel = new JPanel();
        resizablePanel.setBackground(Color.CYAN);
        resizablePanel.setBorder(BorderFactory.createTitledBorder("Resizable Panel"));
        resizablePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = e.getComponent().getSize();
                logEvent("ComponentListener", "componentResized",
                        "New size: " + size.width + "x" + size.height);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                Point location = e.getComponent().getLocation();
                logEvent("ComponentListener", "componentMoved",
                        "New location: (" + location.x + "," + location.y + ")");
            }

            @Override
            public void componentShown(ComponentEvent e) {
                logEvent("ComponentListener", "componentShown",
                        "Component became visible");
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                logEvent("ComponentListener", "componentHidden",
                        "Component became hidden");
            }
        });
        panel.add(resizablePanel);

        // 14. ContainerListener - Component addition/removal
        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(BorderFactory.createTitledBorder("Container Panel"));
        containerPanel.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                logEvent("ContainerListener", "componentAdded",
                        "Added: " + e.getChild().getClass().getSimpleName());
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                logEvent("ContainerListener", "componentRemoved",
                        "Removed: " + e.getChild().getClass().getSimpleName());
            }
        });

        JButton addBtn = new JButton("Add Component");
        addBtn.addActionListener(e -> {
            JLabel label = new JLabel("New Label " + System.currentTimeMillis());
            containerPanel.add(label);
            containerPanel.revalidate();
            containerPanel.repaint();
        });

        JButton removeBtn = new JButton("Remove Component");
        removeBtn.addActionListener(e -> {
            if (containerPanel.getComponentCount() > 0) {
                containerPanel.remove(containerPanel.getComponentCount() - 1);
                containerPanel.revalidate();
                containerPanel.repaint();
            }
        });

        panel.add(containerPanel);
        panel.add(addBtn);
        panel.add(removeBtn);

        // 15. HierarchyListener - Component hierarchy changes
        JPanel hierarchyPanel = new JPanel();
        hierarchyPanel.setBorder(BorderFactory.createTitledBorder("Hierarchy Panel"));
        hierarchyPanel.addHierarchyListener(e -> {
            String changeType = "";
            if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
                changeType += "PARENT_CHANGED ";
            }
            if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
                changeType += "DISPLAYABILITY_CHANGED ";
            }
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                changeType += "SHOWING_CHANGED ";
            }
            logEvent("HierarchyListener", "hierarchyChanged",
                    "Changes: " + changeType);
        });
        panel.add(hierarchyPanel);

        return panel;
    }

    private JPanel createWindowEventsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Window event handlers are added to the main frame in setupAllEventHandlers()
        // Here we create buttons to trigger window events
        JButton newWindowBtn = new JButton("Open New Window");
        newWindowBtn.setToolTipText("Creates a new window with all window event listeners");
        newWindowBtn.addActionListener(e -> createTestWindow());
        panel.add(newWindowBtn);

        JButton iconifyBtn = new JButton("Minimize Main Window");
        iconifyBtn.addActionListener(e -> setState(JFrame.ICONIFIED));
        panel.add(iconifyBtn);

        JButton restoreBtn = new JButton("Restore Main Window");
        restoreBtn.addActionListener(e -> setState(JFrame.NORMAL));
        panel.add(restoreBtn);

        // Internal Frame for InternalFrameListener
        JDesktopPane desktop = new JDesktopPane();
        desktop.setPreferredSize(new Dimension(300, 200));

        JInternalFrame internalFrame = new JInternalFrame("Internal Frame", true, true, true, true);
        internalFrame.setSize(200, 150);
        internalFrame.setLocation(20, 20);

        // 16. InternalFrameListener - Internal frame events
        internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameOpened", "Internal frame opened");
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameClosing", "Internal frame closing");
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameClosed", "Internal frame closed");
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameIconified", "Internal frame iconified");
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameDeiconified", "Internal frame deiconified");
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameActivated", "Internal frame activated");
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                logEvent("InternalFrameListener", "internalFrameDeactivated", "Internal frame deactivated");
            }
        });

        internalFrame.setVisible(true);
        desktop.add(internalFrame);
        panel.add(desktop);

        return panel;
    }

    private JPanel createAdvancedEventsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 17. TreeSelectionListener & TreeExpansionListener - Tree events
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Node 1");
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Node 2");
        node1.add(new DefaultMutableTreeNode("Child 1.1"));
        node1.add(new DefaultMutableTreeNode("Child 1.2"));
        root.add(node1);
        root.add(node2);

        JTree tree = new JTree(root);
        tree.addTreeSelectionListener(e -> {
            TreePath path = e.getNewLeadSelectionPath();
            if (path != null) {
                logEvent("TreeSelectionListener", "valueChanged",
                        "Selected: " + path.getLastPathComponent());
            }
        });

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent e) {
                logEvent("TreeExpansionListener", "treeExpanded",
                        "Expanded: " + e.getPath().getLastPathComponent());
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent e) {
                logEvent("TreeExpansionListener", "treeCollapsed",
                        "Collapsed: " + e.getPath().getLastPathComponent());
            }
        });

        // 18. TreeWillExpandListener - Tree expansion veto
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent e) {
                logEvent("TreeWillExpandListener", "treeWillExpand",
                        "Will expand: " + e.getPath().getLastPathComponent());
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent e) {
                logEvent("TreeWillExpandListener", "treeWillCollapse",
                        "Will collapse: " + e.getPath().getLastPathComponent());
            }
        });

        panel.add(new JScrollPane(tree));

        // 19. TableModelListener - Table data changes
        String[] columnNames = {"Name", "Value"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        tableModel.addRow(new Object[]{"Row 1", "Value 1"});
        tableModel.addRow(new Object[]{"Row 2", "Value 2"});

        tableModel.addTableModelListener(e -> {
            String eventType = "";
            switch (e.getType()) {
                case TableModelEvent.INSERT:
                    eventType = "INSERT";
                    break;
                case TableModelEvent.UPDATE:
                    eventType = "UPDATE";
                    break;
                case TableModelEvent.DELETE:
                    eventType = "DELETE";
                    break;
            }
            logEvent("TableModelListener", "tableChanged",
                    "Event: " + eventType + " Row: " + e.getFirstRow());
        });

        JTable table = new JTable(tableModel);
        panel.add(new JScrollPane(table));

        JButton addRowBtn = new JButton("Add Table Row");
        addRowBtn.addActionListener(e
                -> tableModel.addRow(new Object[]{"New Row", "New Value"}));
        panel.add(addRowBtn);

        // 20. MenuListener - Menu events
        JMenuBar menuBar = new JMenuBar();
        JMenu testMenu = new JMenu("Test Menu");
        testMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                logEvent("MenuListener", "menuSelected", "Menu selected: " + e.getSource());
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                logEvent("MenuListener", "menuDeselected", "Menu deselected");
            }

            @Override
            public void menuCanceled(MenuEvent e) {
                logEvent("MenuListener", "menuCanceled", "Menu canceled");
            }
        });

        testMenu.add(new JMenuItem("Test Item"));
        menuBar.add(testMenu);
        setJMenuBar(menuBar);

        // 21. PopupMenuListener - Popup menu events
        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem("Popup Item 1"));
        popup.add(new JMenuItem("Popup Item 2"));

        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                logEvent("PopupMenuListener", "popupMenuWillBecomeVisible", "Popup becoming visible");
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                logEvent("PopupMenuListener", "popupMenuWillBecomeInvisible", "Popup becoming invisible");
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                logEvent("PopupMenuListener", "popupMenuCanceled", "Popup canceled");
            }
        });

        JButton popupBtn = new JButton("Show Popup Menu");
        popupBtn.addActionListener(e -> popup.show(popupBtn, 0, popupBtn.getHeight()));
        panel.add(popupBtn);

        // 22. PropertyChangeListener - Bean property changes
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(25);
        progressBar.addPropertyChangeListener("value", e
                -> logEvent("PropertyChangeListener", "propertyChange",
                        "Property: " + e.getPropertyName() + " Old: " + e.getOldValue() + " New: " + e.getNewValue()));

        JButton progressBtn = new JButton("Change Progress");
        progressBtn.addActionListener(e
                -> progressBar.setValue((int) (Math.random() * 100)));

        panel.add(progressBar);
        panel.add(progressBtn);

        return panel;
    }

    private JPanel createTextEventsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 23. DocumentListener - Document content changes
        JTextArea textArea = new JTextArea("Edit this text to see DocumentListener events", 5, 20);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                logEvent("DocumentListener", "insertUpdate",
                        "Text inserted at offset: " + e.getOffset() + " length: " + e.getLength());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                logEvent("DocumentListener", "removeUpdate",
                        "Text removed at offset: " + e.getOffset() + " length: " + e.getLength());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                logEvent("DocumentListener", "changedUpdate",
                        "Text attributes changed at offset: " + e.getOffset());
            }
        });
        panel.add(new JScrollPane(textArea));

        // 24. UndoableEditListener - Undoable edit events
        JTextArea undoArea = new JTextArea("Type here for undo events", 3, 20);
        undoArea.getDocument().addUndoableEditListener(e -> {
            UndoableEdit edit = e.getEdit();
            logEvent("UndoableEditListener", "undoableEditHappened",
                    "Edit: " + edit.getPresentationName() + " Significant: " + edit.isSignificant());
        });
        panel.add(new JScrollPane(undoArea));

        // 25. CaretListener - Text caret position changes
        JTextArea caretArea = new JTextArea("Move cursor here", 3, 20);
        caretArea.addCaretListener(e
                -> logEvent("CaretListener", "caretUpdate",
                        "Caret position: " + e.getDot() + " Mark: " + e.getMark()));
        panel.add(new JScrollPane(caretArea));

        // 26. HyperlinkListener - Hyperlink events
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setText("<html><body><a href='#test'>Click this link</a></body></html>");
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(e -> {
            String eventType = e.getEventType().toString();
            logEvent("HyperlinkListener", "hyperlinkUpdate",
                    "Event: " + eventType + " URL: " + e.getURL());
        });
        panel.add(new JScrollPane(editorPane));

        return panel;
    }

    private void setupAllEventHandlers() {
        // Window Event Handlers for main frame

        // 27. WindowListener - Window lifecycle events
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                logEvent("WindowListener", "windowOpened", "Main window opened");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                logEvent("WindowListener", "windowClosing", "Main window closing");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                logEvent("WindowListener", "windowClosed", "Main window closed");
            }

            @Override
            public void windowIconified(WindowEvent e) {
                logEvent("WindowListener", "windowIconified", "Main window iconified");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                logEvent("WindowListener", "windowDeiconified", "Main window deiconified");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                logEvent("WindowListener", "windowActivated", "Main window activated");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                logEvent("WindowListener", "windowDeactivated", "Main window deactivated");
            }
        });

        // 28. WindowFocusListener - Window focus events
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                logEvent("WindowFocusListener", "windowGainedFocus", "Main window gained focus");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                logEvent("WindowFocusListener", "windowLostFocus", "Main window lost focus");
            }
        });

        // 29. WindowStateListener - Window state changes
        addWindowStateListener(e -> {
            String stateString = "";
            int state = e.getNewState();
            if ((state & Frame.ICONIFIED) != 0) {
                stateString += "ICONIFIED ";
            }
            if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                stateString += "MAXIMIZED ";
            }
            if (state == Frame.NORMAL) {
                stateString = "NORMAL";
            }

            logEvent("WindowStateListener", "windowStateChanged",
                    "New state: " + stateString + " (Code: " + state + ")");
        });

        // 30. AncestorListener - Component ancestor changes
        getRootPane().addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                logEvent("AncestorListener", "ancestorAdded",
                        "Ancestor added: " + event.getAncestor().getClass().getSimpleName());
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                logEvent("AncestorListener", "ancestorRemoved",
                        "Ancestor removed: " + event.getAncestor().getClass().getSimpleName());
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                logEvent("AncestorListener", "ancestorMoved",
                        "Ancestor moved: " + event.getAncestor().getClass().getSimpleName());
            }
        });
    }

    private void createTestWindow() {
        JFrame testWindow = new JFrame("Test Window for Window Events");
        testWindow.setSize(300, 200);
        testWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add all window listeners to the test window
        testWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                logEvent("WindowListener (Test)", "windowOpened", "Test window opened");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                logEvent("WindowListener (Test)", "windowClosing", "Test window closing");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                logEvent("WindowListener (Test)", "windowClosed", "Test window closed");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                logEvent("WindowListener (Test)", "windowActivated", "Test window activated");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                logEvent("WindowListener (Test)", "windowDeactivated", "Test window deactivated");
            }
        });

        testWindow.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                logEvent("WindowFocusListener (Test)", "windowGainedFocus", "Test window gained focus");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                logEvent("WindowFocusListener (Test)", "windowLostFocus", "Test window lost focus");
            }
        });

        testWindow.addWindowStateListener(e -> {
            String stateString = "";
            int state = e.getNewState();
            if ((state & Frame.ICONIFIED) != 0) {
                stateString += "ICONIFIED ";
            }
            if ((state & Frame.MAXIMIZED_BOTH) != 0) {
                stateString += "MAXIMIZED ";
            }
            if (state == Frame.NORMAL) {
                stateString = "NORMAL";
            }

            logEvent("WindowStateListener (Test)", "windowStateChanged",
                    "Test window state: " + stateString);
        });

        // Add content to test window
        JPanel content = new JPanel(new FlowLayout());
        content.add(new JLabel("This is a test window"));

        JButton closeBtn = new JButton("Close Window");
        closeBtn.addActionListener(e -> testWindow.dispose());
        content.add(closeBtn);

        JButton minimizeBtn = new JButton("Minimize");
        minimizeBtn.addActionListener(e -> testWindow.setState(Frame.ICONIFIED));
        content.add(minimizeBtn);

        testWindow.add(content);
        testWindow.setVisible(true);
    }

    private void logEvent(String listenerType, String methodName, String details) {
        eventCounter++;
        String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
        String logEntry = String.format("[%d] %s | %s.%s() | %s%n",
                eventCounter, timestamp, listenerType, methodName, details);

        eventLog.append(logEntry);
        eventLog.setCaretPosition(eventLog.getDocument().getLength());
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            FlatLightLaf.setup();
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            CompleteEventHandlersApp app = new CompleteEventHandlersApp();
            app.setVisible(true);
            app.setLocationRelativeTo(null); // centers window
        });
    }
}

/*
 * COMPLETE EVENT HANDLERS SUMMARY:
 * 
 * This application demonstrates 30+ event listener interfaces with 70+ methods:
 * 
 * BASIC EVENT LISTENERS:
 * 1. ActionListener - actionPerformed() - Button clicks, menu selections
 * 2. ItemListener - itemStateChanged() - Checkbox, radio button, combobox changes
 * 3. ChangeListener - stateChanged() - Slider, spinner, progress bar changes
 * 4. ListSelectionListener - valueChanged() - JList selection changes
 * 
 * MOUSE & KEY LISTENERS:
 * 5. MouseListener - mouseClicked(), mousePressed(), mouseReleased(), mouseEntered(), mouseExited()
 * 6. MouseMotionListener - mouseDragged(), mouseMoved()
 * 7. MouseWheelListener - mouseWheelMoved()
 * 8. KeyListener - keyPressed(), keyReleased(), keyTyped()
 * 
 * COMPONENT LISTENERS:
 * 9. FocusListener - focusGained(), focusLost()
 * 10. ComponentListener - componentResized(), componentMoved(), componentShown(), componentHidden()
 * 11. ContainerListener - componentAdded(), componentRemoved()
 * 12. HierarchyListener - hierarchyChanged()
 * 13. AncestorListener - ancestorAdded(), ancestorRemoved(), ancestorMoved()
 * 
 * WINDOW LISTENERS:
 * 14. WindowListener - windowOpened(), windowClosing(), windowClosed(), windowActivated(), 
 *                      windowDeactivated(), windowIconified(), windowDeiconified()
 * 15. WindowFocusListener - windowGainedFocus(), windowLostFocus()
 * 16. WindowStateListener - windowStateChanged()
 * 17. InternalFrameListener - internalFrameOpened(), internalFrameClosing(), internalFrameClosed(),
 *                            internalFrameActivated(), internalFrameDeactivated(), 
 *                            internalFrameIconified(), internalFrameDeiconified()
 * 
 * ADVANCED LISTENERS:
 * 18. TreeSelectionListener - valueChanged()
 * 19. TreeExpansionListener - treeExpanded(), treeCollapsed()
 * 20. TreeWillExpandListener - treeWillExpand(), treeWillCollapse()
 * 21. TableModelListener - tableChanged()
 * 22. MenuListener - menuSelected(), menuDeselected(), menuCanceled()
 * 23. PopupMenuListener - popupMenuWillBecomeVisible(), popupMenuWillBecomeInvisible(), popupMenuCanceled()
 * 24. PropertyChangeListener - propertyChange()
 * 
 * TEXT & DOCUMENT LISTENERS:
 * 25. DocumentListener - insertUpdate(), removeUpdate(), changedUpdate()
 * 26. UndoableEditListener - undoableEditHappened()
 * 27. CaretListener - caretUpdate()
 * 28. HyperlinkListener - hyperlinkUpdate()
 * 
 * ADAPTER CLASSES (Convenience classes that implement listeners with empty methods):
 * - MouseAdapter (implements MouseListener, MouseMotionListener, MouseWheelListener)
 * - KeyAdapter (implements KeyListener)
 * - FocusAdapter (implements FocusListener)
 * - ComponentAdapter (implements ComponentListener)
 * - ContainerAdapter (implements ContainerListener)
 * - WindowAdapter (implements WindowListener, WindowFocusListener, WindowStateListener)
 * - InternalFrameAdapter (implements InternalFrameListener)
 * - MouseMotionAdapter (implements MouseMotionListener)
 * 
 * USAGE PATTERNS:
 * - Use ActionListener for button clicks, menu selections, Enter key in text fields
 * - Use MouseListener for detecting mouse button presses, releases, and hover states
 * - Use KeyListener for keyboard input handling and shortcuts
 * - Use FocusListener for field validation and UI state management
 * - Use DocumentListener for real-time text change detection
 * - Use ComponentListener for responsive layout management
 * - Use WindowListener for application lifecycle management
 * - Use TreeListener for tree navigation and expansion state management
 * - Use TableModelListener for data synchronization
 * - Use PropertyChangeListener for bean property binding
 * 
 * LEARNING TIPS:
 * 1. Most listeners follow the Observer pattern
 * 2. Adapter classes help reduce boilerplate code
 * 3. Event objects contain detailed information about what happened
 * 4. Some events can be consumed to prevent further processing
 * 5. Lambda expressions can simplify single-method listeners
 * 6. Be careful with listeners that fire frequently (like mouseMoved)
 * 7. Always remove listeners to prevent memory leaks in dynamic UIs
 */
