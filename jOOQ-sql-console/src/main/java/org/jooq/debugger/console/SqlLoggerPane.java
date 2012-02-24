/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 *                          Christopher Deckers, chrriis@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.debugger.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.jooq.debugger.SqlQueryDebugger;
import org.jooq.debugger.SqlQueryDebuggerData;
import org.jooq.debugger.SqlQueryDebuggerRegister;
import org.jooq.debugger.SqlQueryDebuggerResultSetData;
import org.jooq.debugger.SqlQueryType;
import org.jooq.debugger.console.misc.JTableX;
import org.jooq.debugger.console.misc.RichTextTransferable;
import org.jooq.debugger.console.misc.Utils;
import org.jooq.debugger.console.misc.XTableColumnModel;


/**
 * @author Christopher Deckers
 */
public class SqlLoggerPane extends JPanel {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

    private static final int COLUMN_LINE = 0;
    private static final int COLUMN_THREAD = 1;
    private static final int COLUMN_TIMESTAMP = 2;
    private static final int COLUMN_PS_PREPARATION_DURATION = 3;
    private static final int COLUMN_PS_BINDING_DURATION = 4;
    private static final int COLUMN_EXEC_TIME = 5;
    private static final int COLUMN_RS_LIFETIME = 6;
    private static final int COLUMN_RS_READ = 7;
    private static final int COLUMN_RS_READ_ROWS = 8;
    private static final int COLUMN_DUPLICATION_COUNT = 9;
    private static final int COLUMN_QUERY = 10;
    private static final int COLUMN_COUNT = COLUMN_QUERY + 1;

    private SqlQueryDebugger sqlQueryDebugger;
    private JTableX table;
    private SqlTextArea textArea;
    private JLabel loggerStatusLabel;
    private JButton loggerLoggingOnButton;
    private JButton loggerLoggingOffButton;
    private boolean isLogging;
    private boolean isReadQueryTypeDisplayed = true;
    private boolean isWriteQueryTypeDisplayed = true;
    private boolean isOtherQueryTypeDisplayed = true;
    private boolean isScrollLocked;

    public SqlLoggerPane() {
        super(new BorderLayout());
        setOpaque(false);
        JPanel loggerHeaderPanel = new JPanel(new BorderLayout());
        loggerHeaderPanel.setOpaque(false);
        JToolBar loggerHeaderWestPanel = new JToolBar();
        loggerHeaderWestPanel.setFloatable(false);
        loggerHeaderWestPanel.setOpaque(false);
        loggerLoggingOnButton = new JButton(new ImageIcon(getClass().getResource("resources/Running16.png")));
        loggerLoggingOnButton.setOpaque(false);
        loggerLoggingOnButton.setFocusable(false);
        loggerLoggingOnButton.setToolTipText("Activate logging");
        loggerLoggingOnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLogging(true);
            }
        });
        loggerHeaderWestPanel.add(loggerLoggingOnButton);
        loggerLoggingOffButton = new JButton(new ImageIcon(getClass().getResource("resources/Paused16.png")));
        loggerLoggingOffButton.setOpaque(false);
        loggerLoggingOffButton.setFocusable(false);
        loggerLoggingOffButton.setToolTipText("Deactivate logging");
        loggerLoggingOffButton.setVisible(false);
        loggerLoggingOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLogging(false);
                loggerLoggingOnButton.requestFocus();
            }
        });
        loggerHeaderWestPanel.add(loggerLoggingOffButton);
        JButton loggerClearButton = new JButton(new ImageIcon(getClass().getResource("resources/Clear16.png")));
        loggerClearButton.setOpaque(false);
        loggerClearButton.setFocusable(false);
        loggerClearButton.setToolTipText("Clear collected data");
        loggerClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryDebuggingInfoList.clear();
                textArea.setText("");
                int originalRowCount = displayedQueryDebuggingInfoList.size();
                displayedQueryDebuggingInfoList.clear();
                queriesToCountMap.clear();
                if(originalRowCount > 0) {
                    ((AbstractTableModel)table.getModel()).fireTableRowsDeleted(0, originalRowCount - 1);
                }
                updateStatusLabel();
            }
        });
        loggerHeaderWestPanel.add(loggerClearButton);
        JToggleButton scrollLockButton = new JToggleButton(new ImageIcon(getClass().getResource("resources/LockScroll16.png")));
        scrollLockButton.setFocusable(false);
        scrollLockButton.setToolTipText("Scroll Lock");
        scrollLockButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isScrollLocked = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        loggerHeaderWestPanel.add(scrollLockButton);
        loggerHeaderPanel.add(loggerHeaderWestPanel, BorderLayout.WEST);
        JPanel loggerHeaderEastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        loggerHeaderEastPanel.setOpaque(false);
        JCheckBox loggerThreadCheckBox = new JCheckBox("Threads", true);
        loggerThreadCheckBox.setOpaque(false);
        loggerThreadCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isThreadDisplayed = e.getStateChange() == ItemEvent.SELECTED;
                XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_THREAD), isThreadDisplayed);
                table.adjustLastColumn();
            }
        });
        loggerHeaderEastPanel.add(loggerThreadCheckBox);
        JCheckBox loggerTimestampCheckBox = new JCheckBox("Timestamps", true);
        loggerTimestampCheckBox.setOpaque(false);
        loggerTimestampCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isTimestampDisplayed = e.getStateChange() == ItemEvent.SELECTED;
                XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_TIMESTAMP), isTimestampDisplayed);
                table.adjustLastColumn();
            }
        });
        loggerHeaderEastPanel.add(loggerTimestampCheckBox);
        JCheckBox preparedStatementDataCheckBox = new JCheckBox("PS Data", true);
        preparedStatementDataCheckBox.setOpaque(false);
        preparedStatementDataCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isPreparedStatementDataShown = e.getStateChange() == ItemEvent.SELECTED;
                XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_PS_PREPARATION_DURATION), isPreparedStatementDataShown);
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_PS_BINDING_DURATION), isPreparedStatementDataShown);
                table.adjustLastColumn();
            }
        });
        loggerHeaderEastPanel.add(preparedStatementDataCheckBox);
        JCheckBox loggerDurationCheckBox = new JCheckBox("Exec Time", true);
        loggerDurationCheckBox.setOpaque(false);
        loggerDurationCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isDurationDisplayed = e.getStateChange() == ItemEvent.SELECTED;
                XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_EXEC_TIME), isDurationDisplayed);
                table.adjustLastColumn();
            }
        });
        loggerHeaderEastPanel.add(loggerDurationCheckBox);
        JCheckBox resultSetDataCheckBox = new JCheckBox("RS Data", true);
        resultSetDataCheckBox.setOpaque(false);
        resultSetDataCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isResultSetDataShown = e.getStateChange() == ItemEvent.SELECTED;
                XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_RS_LIFETIME), isResultSetDataShown);
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_RS_READ), isResultSetDataShown);
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_RS_READ_ROWS), isResultSetDataShown);
                table.adjustLastColumn();
            }
        });
        loggerHeaderEastPanel.add(resultSetDataCheckBox);
        JCheckBox duplicationCountCheckBox = new JCheckBox("Duplication", true);
        duplicationCountCheckBox.setOpaque(false);
        duplicationCountCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isDuplicationCountShown = e.getStateChange() == ItemEvent.SELECTED;
                XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                columnModel.setColumnVisible(columnModel.getColumnByModelIndex(COLUMN_DUPLICATION_COUNT), isDuplicationCountShown);
                table.adjustLastColumn();
            }
        });
        loggerHeaderEastPanel.add(duplicationCountCheckBox);
        loggerHeaderEastPanel.add(Box.createHorizontalStrut(10));
        JCheckBox loggerReadQueryTypeCheckBox = new JCheckBox("Read", isReadQueryTypeDisplayed);
        loggerReadQueryTypeCheckBox.setOpaque(false);
        loggerReadQueryTypeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isReadQueryTypeDisplayed = e.getStateChange() == ItemEvent.SELECTED;
                refreshRows();
            }
        });
        loggerHeaderEastPanel.add(loggerReadQueryTypeCheckBox);
        JCheckBox loggerWriteQueryTypeCheckBox = new JCheckBox("Write", isWriteQueryTypeDisplayed);
        loggerWriteQueryTypeCheckBox.setOpaque(false);
        loggerWriteQueryTypeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isWriteQueryTypeDisplayed = e.getStateChange() == ItemEvent.SELECTED;
                refreshRows();
            }
        });
        loggerHeaderEastPanel.add(loggerWriteQueryTypeCheckBox);
        JCheckBox loggerOtherQueryTypeCheckBox = new JCheckBox("Other", isOtherQueryTypeDisplayed);
        loggerOtherQueryTypeCheckBox.setOpaque(false);
        loggerOtherQueryTypeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                isOtherQueryTypeDisplayed = e.getStateChange() == ItemEvent.SELECTED;
                refreshRows();
            }
        });
        loggerHeaderEastPanel.add(loggerOtherQueryTypeCheckBox);
        loggerHeaderPanel.add(loggerHeaderEastPanel, BorderLayout.CENTER);
        add(loggerHeaderPanel, BorderLayout.NORTH);
        table = new JTableX(new AbstractTableModel() {
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                QueryDebuggingInfo queryDebuggingInfo = displayedQueryDebuggingInfoList.get(rowIndex);
                switch(columnIndex) {
                    case COLUMN_LINE: {
                        return rowIndex + 1;
                    }
                    case COLUMN_THREAD: {
                        return queryDebuggingInfo.getThreadName() + " [" + queryDebuggingInfo.getThreadId() + "]";
                    }
                    case COLUMN_TIMESTAMP: {
                        return TIMESTAMP_FORMAT.format(new Date(queryDebuggingInfo.getTimestamp()));
                    }
                    case COLUMN_PS_PREPARATION_DURATION: {
                        Long duration = queryDebuggingInfo.getPrepardeStatementPreparationDuration();
                        return duration == null? null: duration;
                    }
                    case COLUMN_PS_BINDING_DURATION: {
                        Long duration = queryDebuggingInfo.getPrepardeStatementBindingDuration();
                        return duration == null? null: duration;
                    }
                    case COLUMN_EXEC_TIME: {
                        long duration = queryDebuggingInfo.getExecutionDuration();
                        return duration < 0? null: duration;
                    }
                    case COLUMN_RS_LIFETIME: {
                        SqlQueryDebuggerResultSetData rsData = queryDebuggingInfo.getSqlQueryDebuggerResultSetData();
                        return rsData == null? null: rsData.getLifeTime();
                    }
                    case COLUMN_RS_READ: {
                        SqlQueryDebuggerResultSetData rsData = queryDebuggingInfo.getSqlQueryDebuggerResultSetData();
                        return rsData == null? null: rsData.getReadCount();
                    }
                    case COLUMN_RS_READ_ROWS: {
                        SqlQueryDebuggerResultSetData rsData = queryDebuggingInfo.getSqlQueryDebuggerResultSetData();
                        return rsData == null? null: rsData.getReadRows();
                    }
                    case COLUMN_DUPLICATION_COUNT: {
                        return queryDebuggingInfo.getDuplicationCount();
                    }
                    case COLUMN_QUERY: {
                        StringBuilder querySB = new StringBuilder();
                        String[] queries = queryDebuggingInfo.getQueries();
                        for(int i=0; i<queries.length; i++) {
                            if(i > 0) {
                                querySB.append(LS);
                            }
                            String s = queries[i];
                            querySB.append(s.trim());
                        }
                        return querySB.toString();
                    }
                }
                return null;
            }

            @Override
            public int getRowCount() {
                return displayedQueryDebuggingInfoList.size();
            }

            @Override
            public int getColumnCount() {
                return COLUMN_COUNT;
            }

            @Override
            public String getColumnName(int column) {
                switch(column) {
                    case COLUMN_LINE:
                        return "Line";
                    case COLUMN_THREAD:
                        return "Thread";
                    case COLUMN_TIMESTAMP:
                        return "Timestamp";
                    case COLUMN_PS_PREPARATION_DURATION:
                        return "PS preparation (ms)";
                    case COLUMN_PS_BINDING_DURATION:
                        return "PS binding (ms)";
                    case COLUMN_EXEC_TIME:
                        return "Exec time (ms)";
                    case COLUMN_RS_LIFETIME:
                        return "RS lifetime (ms)";
                    case COLUMN_RS_READ:
                        return "RS read";
                    case COLUMN_RS_READ_ROWS:
                        return "RS rows";
                    case COLUMN_DUPLICATION_COUNT:
                        return "Duplic.";
                    case COLUMN_QUERY:
                        return "Query";
                }
                return null;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch(columnIndex) {
                    case COLUMN_LINE: return Integer.class;
                    case COLUMN_PS_PREPARATION_DURATION: return Long.class;
                    case COLUMN_PS_BINDING_DURATION: return Long.class;
                    case COLUMN_EXEC_TIME: return Long.class;
                    case COLUMN_RS_LIFETIME: return Long.class;
                    case COLUMN_RS_READ: return Integer.class;
                    case COLUMN_RS_READ_ROWS: return Integer.class;
                    case COLUMN_DUPLICATION_COUNT: return Integer.class;
                }
                return super.getColumnClass(columnIndex);
            }

        })/* {
            @Override
            public String getToolTipText(MouseEvent e) {
                return createMultilineTooltip(e);
            }
        }*/;
        registerTooltip();
        table.setAutoCreateRowSorter(true);
        table.getRowSorter().setSortKeys(Arrays.asList(new RowSorter.SortKey(COLUMN_LINE, SortOrder.ASCENDING)));
        XTableColumnModel columnModel = new XTableColumnModel();
        table.setColumnModel(columnModel);
        table.createDefaultColumnsFromModel();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        columnModel.getColumnByModelIndex(COLUMN_LINE).setPreferredWidth(30);
        columnModel.getColumnByModelIndex(COLUMN_TIMESTAMP).setPreferredWidth(80);
        columnModel.getColumnByModelIndex(COLUMN_THREAD).setPreferredWidth(150);
        columnModel.getColumnByModelIndex(COLUMN_DUPLICATION_COUNT).setPreferredWidth(40);
        table.setColumnSelectionAllowed(true);
        table.setFillsViewportHeight(true);
//        ToolTipManager.sharedInstance().registerComponent(table);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
            private void maybeShowPopup(MouseEvent e) {
                if(e.isPopupTrigger()) {
                    Point location = e.getPoint();
                    int row = table.rowAtPoint(location);
                    if(row < 0) {
                        return;
                    }
                    int column = table.columnAtPoint(location);
                    if(column < 0) {
                        return;
                    }
                    if(!table.isCellSelected(row, column)) {
                        ListSelectionModel selectionModel = table.getSelectionModel();
                        selectionModel.clearSelection();
                        selectionModel.addSelectionInterval(row, row);
                    }
                    JPopupMenu popupMenu = new JPopupMenu();
                    int[] selectedRows = table.getSelectedRows();
                    final QueryDebuggingInfo[] selectedQueryDebuggingInfos = new QueryDebuggingInfo[selectedRows.length];
                    for(int i=0; i<selectedRows.length; i++) {
                        selectedQueryDebuggingInfos[i] = displayedQueryDebuggingInfoList.get(table.convertRowIndexToModel(selectedRows[i]));
                    }
                    if(selectedQueryDebuggingInfos.length > 0) {
                        JMenuItem copyToClipboardMenuItem = new JMenuItem("Copy " + (selectedQueryDebuggingInfos.length > 1? selectedQueryDebuggingInfos.length + " ": "") + "Statement" + (selectedQueryDebuggingInfos.length == 1? "": "s") + " to Clipboard");
                        copyToClipboardMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                copyToClipboard(selectedQueryDebuggingInfos);
                            }
                        });
                        popupMenu.add(copyToClipboardMenuItem);
                    }
                    if(displayedQueryDebuggingInfoList.size() > 0) {
                        JMenuItem copyAllToClipboardMenuItem = new JMenuItem("Copy All Statements (" + displayedQueryDebuggingInfoList.size() + ") to Clipboard");
                        copyAllToClipboardMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                copyToClipboard(displayedQueryDebuggingInfoList.toArray(new QueryDebuggingInfo[0]));
                            }
                        });
                        popupMenu.add(copyAllToClipboardMenuItem);
                    }
                    if(selectedQueryDebuggingInfos.length == 1) {
                        JMenuItem dumpStackMenuItem = new JMenuItem("Dump Statement Call Stack");
                        dumpStackMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                selectedQueryDebuggingInfos[0].getThrowable().printStackTrace();
                            }
                        });
                        popupMenu.add(dumpStackMenuItem);
                        JMenuItem copyStackToClipboardMenuItem = new JMenuItem("Copy Statement Call Stack to Clipboard");
                        copyStackToClipboardMenuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                StringWriter sw = new StringWriter();
                                selectedQueryDebuggingInfos[0].getThrowable().printStackTrace(new PrintWriter(sw));
                                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clipboard.setContents(new StringSelection(sw.toString()), null);
                            }
                        });
                        popupMenu.add(copyStackToClipboardMenuItem);
                    }
                    if(popupMenu.getComponentCount() > 0) {
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()) {
                    return;
                }
                int[] selectedRows = table.getSelectedRows();
                String text;
                if(selectedRows.length > 30) {
                    text = "(Too many selected rows)";
                } else {
                    StringBuilder sb = new StringBuilder();
                    for(int row: selectedRows) {
                        row = table.convertRowIndexToModel(row);
                        QueryDebuggingInfo queryDebuggingInfo = displayedQueryDebuggingInfoList.get(row);
                        for(String query: queryDebuggingInfo.getQueries()) {
                            sb.append(query.trim()).append(LS);
                        }
                    }
                    text = sb.toString();
                }
                if(!text.equals(textArea.getText())) {
                    textArea.setText(text);
                    textArea.setCaretPosition(0);
                }
                updateStatusLabel();
            }
        });
        textArea = new SqlTextArea();
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(table), new JScrollPane(textArea));
        splitPane.setResizeWeight(1);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                splitPane.setDividerLocation(splitPane.getHeight() - 100);
            }
        });
        add(splitPane, BorderLayout.CENTER);
        loggerStatusLabel = new JLabel();
        updateStatusLabel();
        add(loggerStatusLabel, BorderLayout.SOUTH);
        preparedStatementDataCheckBox.setSelected(false);
        resultSetDataCheckBox.setSelected(false);
        duplicationCountCheckBox.setSelected(false);
        loggerTimestampCheckBox.setSelected(false);
        loggerDurationCheckBox.setSelected(false);
        loggerThreadCheckBox.setSelected(false);
    }

    private List<QueryDebuggingInfo> queryDebuggingInfoList = new ArrayList<QueryDebuggingInfo>();
    private List<QueryDebuggingInfo> displayedQueryDebuggingInfoList = new ArrayList<QueryDebuggingInfo>();
    private Map<List<String>, Integer> queriesToCountMap = new HashMap<List<String>, Integer>();

    private void refreshRows() {
        int originalRowCount = displayedQueryDebuggingInfoList.size();
        displayedQueryDebuggingInfoList.clear();
        queriesToCountMap.clear();
        textArea.setText("");
        if(originalRowCount > 0) {
            ((AbstractTableModel)table.getModel()).fireTableRowsDeleted(0, originalRowCount - 1);
        }
        for(QueryDebuggingInfo queryDebuggingInfo: queryDebuggingInfoList) {
            addDisplayedRow(queryDebuggingInfo);
        }
        int displayedRowCount = displayedQueryDebuggingInfoList.size();
        if(displayedRowCount > 0) {
            ((AbstractTableModel)table.getModel()).fireTableRowsInserted(0, displayedRowCount - 1);
        }
        updateStatusLabel();
    }

    private static final int MAX_NUMBER_OF_ROWS = 10000;

    private void addRow(QueryDebuggingInfo queryDebuggingInfo) {
        if(queryDebuggingInfoList.size() == MAX_NUMBER_OF_ROWS) {
            QueryDebuggingInfo discaredDebuggingInfo = queryDebuggingInfoList.remove(0);
            if(displayedQueryDebuggingInfoList.size() > 0 && displayedQueryDebuggingInfoList.get(0) == discaredDebuggingInfo) {
                displayedQueryDebuggingInfoList.remove(0);
                for(int i=displayedQueryDebuggingInfoList.size()-1; i>=0; i--) {
                    displayedQueryDebuggingInfoList.get(i).setDisplayedRow(i);
                }
                ((AbstractTableModel)table.getModel()).fireTableRowsDeleted(0, 0);
            }
        }
        queryDebuggingInfoList.add(queryDebuggingInfo);
        addDisplayedRow(queryDebuggingInfo);
        int displayedRow = queryDebuggingInfo.getDisplayedRow();
        if(displayedRow >= 0) {
            ((AbstractTableModel)table.getModel()).fireTableRowsInserted(displayedRow, displayedRow);
            if(!isScrollLocked) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Sort the line column if it is the primary sort key in ascending order.
                        List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
                        if(sortKeys.size() >= 1) {
                            SortKey sortKey = sortKeys.get(0);
                            if(sortKey.getColumn() == COLUMN_LINE && sortKey.getSortOrder() == SortOrder.ASCENDING) {
                                table.scrollRectToVisible(new Rectangle(0, table.getHeight() - 1, 1, 1));
                            }
                        }
                    }
                });
            }
        }
        updateStatusLabel();
    }

    private void addDisplayedRow(QueryDebuggingInfo queryDebuggingInfo) {
        boolean isDisplayed = false;
        switch(queryDebuggingInfo.getQueryType()) {
            case READ: isDisplayed = isReadQueryTypeDisplayed; break;
            case WRITE: isDisplayed = isWriteQueryTypeDisplayed; break;
            case OTHER: isDisplayed = isOtherQueryTypeDisplayed; break;
        }
        int displayedRow = -1;
        if(isDisplayed) {
            List<String> queryList = Arrays.asList(queryDebuggingInfo.getQueries());
            Integer count = queriesToCountMap.get(queryList);
            if(count == null) {
                count = 1;
            } else {
                count++;
            }
            queriesToCountMap.put(queryList, count);
            displayedRow = displayedQueryDebuggingInfoList.size();
            queryDebuggingInfo.setDuplicationCount(count);
            displayedQueryDebuggingInfoList.add(queryDebuggingInfo);
        }
        queryDebuggingInfo.setDisplayedRow(displayedRow);
    }

    private void updateRow(QueryDebuggingInfo queryDebuggingInfo) {
        int displayedRow = queryDebuggingInfo.getDisplayedRow();
        if(displayedRow >= 0) {
            ((AbstractTableModel)table.getModel()).fireTableRowsUpdated(displayedRow, displayedRow);
        }
    }

    private static class QueryDebuggingInfo {
        private long timestamp;
        private SqlQueryDebuggerData sqlQueryDebuggerData;
        private Throwable throwable;
        private int duplicationCount;
        public QueryDebuggingInfo(long timestamp, SqlQueryDebuggerData sqlQueryDebuggerData) {
            this.timestamp = timestamp;
            this.sqlQueryDebuggerData = sqlQueryDebuggerData;
            this.throwable = new Exception("Statement Stack trace");
            throwable.setStackTrace(sqlQueryDebuggerData.getCallerStackTraceElements());
        }
        public long getTimestamp() {
            return timestamp;
        }
        public SqlQueryDebuggerData getSqlQueryDebuggerData() {
            return sqlQueryDebuggerData;
        }
        public Long getPrepardeStatementPreparationDuration() {
            return sqlQueryDebuggerData.getPreparedStatementPreparationDuration();
        }
        public Long getPrepardeStatementBindingDuration() {
            return sqlQueryDebuggerData.getPreparedStatementBindingDuration();
        }
        public long getExecutionDuration() {
            return sqlQueryDebuggerData.getExecutionDuration();
        }
        public SqlQueryType getQueryType() {
            return sqlQueryDebuggerData.getQueryType();
        }
        public String[] getQueries() {
            return sqlQueryDebuggerData.getQueries();
        }
        public Throwable getThrowable() {
            return throwable;
        }
        public String getThreadName() {
            return sqlQueryDebuggerData.getThreadName();
        }
        public long getThreadId() {
            return sqlQueryDebuggerData.getThreadID();
        }
        public void setDuplicationCount(int duplicationCount) {
            this.duplicationCount = duplicationCount;
        }
        public int getDuplicationCount() {
            return duplicationCount;
        }
        private SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData;
        public void setSqlQueryDebuggerResultSetData(SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData) {
            this.sqlQueryDebuggerResultSetData = sqlQueryDebuggerResultSetData;
        }
        public SqlQueryDebuggerResultSetData getSqlQueryDebuggerResultSetData() {
            return sqlQueryDebuggerResultSetData;
        }
        private int displayedRow = -1;
        public int getDisplayedRow() {
            return displayedRow;
        }
        public void setDisplayedRow(int displayedRow) {
            this.displayedRow = displayedRow;
        }
    }

    private static String LS = System.getProperty("line.separator");

    public void setLogging(boolean isLogging) {
        if(this.isLogging == isLogging) {
            return;
        }
        this.isLogging = isLogging;
        loggerLoggingOnButton.setVisible(!isLogging);
        loggerLoggingOffButton.setVisible(isLogging);
        if(sqlQueryDebugger != null) {
            SqlQueryDebuggerRegister.removeSqlQueryDebugger(sqlQueryDebugger);
            sqlQueryDebugger = null;
        }
        if(isLogging) {
            sqlQueryDebugger = new SqlQueryDebugger() {
                @Override
                public void debugQueries(SqlQueryDebuggerData sqlQueryDebuggerData) {
                    debugQueries(new QueryDebuggingInfo(System.currentTimeMillis(), sqlQueryDebuggerData));
                }
                public void debugQueries(final QueryDebuggingInfo queryDebuggingInfo) {
                    if(!SwingUtilities.isEventDispatchThread()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                debugQueries(queryDebuggingInfo);
                            }
                        });
                        return;
                    }
                    addRow(queryDebuggingInfo);
                }
                @Override
                public void debugResultSet(final int sqlQueryDebuggerDataID, final SqlQueryDebuggerResultSetData sqlQueryDebuggerResultSetData) {
                    if(!SwingUtilities.isEventDispatchThread()) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                debugResultSet(sqlQueryDebuggerDataID, sqlQueryDebuggerResultSetData);
                            }
                        });
                        return;
                    }
                    for(int i=queryDebuggingInfoList.size()-1; i>=0; i--) {
                        QueryDebuggingInfo queryDebuggingInfo = queryDebuggingInfoList.get(i);
                        if(queryDebuggingInfo.getSqlQueryDebuggerData().getID() == sqlQueryDebuggerDataID) {
                            queryDebuggingInfo.setSqlQueryDebuggerResultSetData(sqlQueryDebuggerResultSetData);
                            XTableColumnModel columnModel = (XTableColumnModel)table.getColumnModel();
                            boolean isResultSetDataShown = columnModel.isColumnVisible(columnModel.getColumnByModelIndex(COLUMN_RS_LIFETIME));
                            if(isResultSetDataShown) {
                                updateRow(queryDebuggingInfo);
                            }
                            break;
                        }
                    }
                }
            };
            SqlQueryDebuggerRegister.addSqlQueryDebugger(sqlQueryDebugger);
        }
    }

    private void updateStatusLabel() {
        int size = queryDebuggingInfoList.size();
        int displayedCount = displayedQueryDebuggingInfoList.size();
        String text;
        if(displayedCount == size) {
            text = size + " queries";
        } else {
            text = displayedCount + "/" + size + " queries";
        }
        int count = table.getSelectedRowCount();
        if(count > 0) {
            text = text + " - " + count + " selected rows";
        }
        loggerStatusLabel.setText(text);
    }

    private String getStackTrace(final QueryDebuggingInfo queryDebuggingInfo) {
        StackTraceElement[] stackTraceElements = queryDebuggingInfo.getThrowable().getStackTrace();
        StringBuilder sb = new StringBuilder();
        // TODO: adjust number of levels to ignore before and after.
        for(int i=1; i<stackTraceElements.length; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
//            if(!stackTraceElement.getClassName().startsWith("org.jooq.")) {
//                break;
//            }
            if(sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(stackTraceElement);
        }
        return sb.toString();
    }

    private void copyToClipboard(QueryDebuggingInfo[] queryDebuggingInfos) {
        StringBuilder stringSB = new StringBuilder();
        StringBuilder htmlSB = new StringBuilder();
        htmlSB.append("<html>\n<body>\n<table>\n");
        htmlSB.append("<tr>" +
                "<th>Thread name</th>" +
                "<th>Thread ID</th>" +
                "<th>Timestamp</th>" +
                "<th>Exec time (ms)</th>" +
                "<th>RS lifetime (ms)</th>" +
                "<th>RS read</th>" +
//                "<th>RS write</th>" +
                "<th>RS rows read</th>" +
                "<th>Query</th>" +
                "<th>Stack trace</th>" +
                "</tr>\n");
        for(QueryDebuggingInfo queryDebuggingInfo: queryDebuggingInfos) {
            SqlQueryDebuggerResultSetData resultSetData = queryDebuggingInfo.getSqlQueryDebuggerResultSetData();
            htmlSB.append("<tr>\n");
            htmlSB.append("<td>");
            htmlSB.append(queryDebuggingInfo.getThreadName());
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(queryDebuggingInfo.getThreadId());
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(TIMESTAMP_FORMAT.format(new Date(queryDebuggingInfo.getTimestamp())));
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(queryDebuggingInfo.getExecutionDuration());
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(resultSetData == null? "": resultSetData.getLifeTime());
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(resultSetData == null? "": resultSetData.getReadCount());
            htmlSB.append("</td>\n");
//            htmlSB.append("<td>");
//            htmlSB.append(resultSetData == null? "": resultSetData.getWriteCount());
//            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(resultSetData == null? "": resultSetData.getReadRows());
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            String[] queries = queryDebuggingInfo.getQueries();
            for(int i=0; i<queries.length; i++) {
                String query = queries[i];
                if(i > 0) {
                    htmlSB.append("\n");
                }
                htmlSB.append(query);
                stringSB.append(query.trim() + LS);
            }
            htmlSB.append("</td>\n");
            htmlSB.append("<td>");
            htmlSB.append(getStackTrace(queryDebuggingInfo));
            htmlSB.append("</td>\n");
            htmlSB.append("</tr>\n");
        }
        htmlSB.append("</table>\n</body>\n</html>");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new RichTextTransferable(htmlSB.toString(), stringSB.toString()), null);
    }

    private void registerTooltip() {
        class TableTipListener extends MouseInputAdapter implements TableModelListener {
            private Timer enterTimer;
            public TableTipListener() {
                enterTimer = new Timer(750, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        processTip();
                    }
                });
                enterTimer.setRepeats(false);
            }
            private Point point;
            @Override
            public void mouseEntered(MouseEvent e) {
                point = SwingUtilities.convertPoint(table, e.getPoint(), table.getParent());
                enterTimer.start();
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                point = SwingUtilities.convertPoint(table, e.getPoint(), table.getParent());
                if(tip == null) {
                    enterTimer.restart();
                } else {
                    processTip();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                enterTimer.stop();
                processTip(null);
            }
            private void processTip() {
                String text = getMultilineTooltip(SwingUtilities.convertPoint(table.getParent(), point, table));
                processTip(text);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                point = null;
                enterTimer.stop();
                processTip(null);
            }
            private String lastText;
            private Popup tip;
            private void processTip(String text) {
                if(Utils.equals(lastText, text)) {
                    return;
                }
                lastText = text;
                if(tip != null) {
                    tip.hide();
                    tip = null;
                }
                if(text != null) {
                    PopupFactory popupFactory = PopupFactory.getSharedInstance();
                    JTextArea textContent = new JTextArea(text);
                    textContent.setFont(UIManager.getFont("ToolTip.font"));
                    textContent.setBackground(UIManager.getColor("ToolTip.background"));
                    textContent.setForeground(UIManager.getColor("ToolTip.foreground"));
                    textContent.setBorder(UIManager.getBorder("ToolTip.border"));
                    Point location = new Point(point);
                    SwingUtilities.convertPointToScreen(location, table.getParent());
                    GraphicsConfiguration gc = table.getGraphicsConfiguration();
                    Rectangle sBounds = gc.getBounds();
                    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
                    sBounds.x += screenInsets.left;
                    sBounds.y += screenInsets.top;
                    sBounds.width -= screenInsets.left + screenInsets.right;
                    sBounds.height -= screenInsets.top + screenInsets.bottom;
                    Dimension tipSize = textContent.getPreferredSize();
//                  tipSize.height = Math.min(tipSize.height, 500);
                    textContent.setPreferredSize(tipSize);
                    location.x += 20;
                    location.x = Math.min(location.x, sBounds.x + sBounds.width - tipSize.width);
                    if(location.y + tipSize.height > sBounds.y + sBounds.height && location.y - 40 - tipSize.height >= sBounds.y) {
                        location.y -= 40 + tipSize.height;
                    }
                    location.y += 20;
                    tip = popupFactory.getPopup(null, textContent, location.x, location.y);
                    tip.show();
                }
            }
            @Override
            public void tableChanged(TableModelEvent e) {
                if(tip != null) {
                    processTip();
                }
            }
        };
        TableTipListener tableTipListener = new TableTipListener();
        table.addMouseListener(tableTipListener);
        table.addMouseMotionListener(tableTipListener);
        table.getModel().addTableModelListener(tableTipListener);
    }

    private String getMultilineTooltip(Point p) {
        int row = table.rowAtPoint(p);
        if(row < 0) {
            return null;
        }
        int column = table.columnAtPoint(p);
        if(column < 0) {
            return null;
        }
        row = table.convertRowIndexToModel(row);
        column = table.convertColumnIndexToModel(column);
        if(column != COLUMN_QUERY) {
            return null;
        }
        final QueryDebuggingInfo queryDebuggingInfo = displayedQueryDebuggingInfoList.get(row);
        if(queryDebuggingInfo != null) {
            return queryDebuggingInfo.getThreadName() + " [" + queryDebuggingInfo.getThreadId() + "]\n" + getStackTrace(queryDebuggingInfo);
        }
        return null;
    }

}