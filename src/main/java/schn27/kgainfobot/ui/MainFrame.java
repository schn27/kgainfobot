/*
 * Copyright (C) 2015 amalikov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package schn27.kgainfobot.ui;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;
import schn27.kgainfobot.Session;
import schn27.kgainfobot.data.Account;
import schn27.kgainfobot.data.AccountsManager;
import schn27.kgainfobot.data.Department;
import schn27.kgainfobot.data.Info;
import schn27.kgainfobot.data.Schedule;
import schn27.kgainfobot.data.Structure;
import schn27.kgainfobot.data.Theme;

/**
 *
 * @author amalikov
 */
public class MainFrame extends javax.swing.JFrame {

	private static final String AccountsFileName = "./accounts.xml";
	private static final String ScheduleFileName = "./schedule.xml";
	private static final String InfoFileName = "./kgainfo.xml";
	
	public static void createAndShow() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
	}
	
	private MainFrame() {
		initAccounts();
		initComponents();
		initSchedule();
		initInfo();
	}
	
	private void initAccounts() {
		accountsManager = new AccountsManager(AccountsFileName);
		accountsManager.loadFromFile();
	}
	
	private void initSchedule() {
		schedule = new Schedule(ScheduleFileName);
		schedule.loadFromFile();
		
		txtStartTime.setText(schedule.getStartTime().toString());
		txtStartTime.addActionListener(this::scheduleActionPerformed);
		txtStartTime.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
			}

			@Override
			public void focusLost(FocusEvent fe) {
				scheduleActionPerformed(new ActionEvent(this, 0, ""));	// fake action event
			}
		});
		
		txtEndTime.setText(schedule.getEndTime().toString());
		txtEndTime.addActionListener(this::scheduleActionPerformed);
		txtEndTime.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
			}

			@Override
			public void focusLost(FocusEvent fe) {
				scheduleActionPerformed(new ActionEvent(this, 0, ""));	// fake action event
			}
		});		
		
		boolean[] weekDays = schedule.getWeekDays();
		chkWeekDay1.setSelected(weekDays[0]);
		chkWeekDay1.addActionListener(this::scheduleActionPerformed);
		chkWeekDay2.setSelected(weekDays[1]);
		chkWeekDay2.addActionListener(this::scheduleActionPerformed);
		chkWeekDay3.setSelected(weekDays[2]);
		chkWeekDay3.addActionListener(this::scheduleActionPerformed);
		chkWeekDay4.setSelected(weekDays[3]);
		chkWeekDay4.addActionListener(this::scheduleActionPerformed);
		chkWeekDay5.setSelected(weekDays[4]);
		chkWeekDay5.addActionListener(this::scheduleActionPerformed);
	}
	
	private void initInfo() {
		info = new Info(InfoFileName);
		info.loadFromFile();
	}
	
	private TableModel getAccountsTableModel() {
		return new AccountsTableModel(accountsManager);
	}	

    private void scheduleActionPerformed(java.awt.event.ActionEvent evt) {
		schedule.setAll(
				txtStartTime.getText(), 
				txtEndTime.getText(), 
				new boolean[]{
					chkWeekDay1.isSelected(),
					chkWeekDay2.isSelected(),
					chkWeekDay3.isSelected(),
					chkWeekDay4.isSelected(),
					chkWeekDay5.isSelected(),
				});
    }
	
	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem addAccount = new javax.swing.JMenuItem();
        removeAccount = new javax.swing.JMenuItem();
        javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        tblAccounts = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        txtStartTime = new javax.swing.JFormattedTextField();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        txtEndTime = new javax.swing.JFormattedTextField();
        chkWeekDay1 = new javax.swing.JCheckBox();
        chkWeekDay2 = new javax.swing.JCheckBox();
        chkWeekDay3 = new javax.swing.JCheckBox();
        chkWeekDay4 = new javax.swing.JCheckBox();
        chkWeekDay5 = new javax.swing.JCheckBox();
        btnGetInfo = new javax.swing.JButton();
        txtGetInfoStatus = new javax.swing.JTextField();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        tblTasks = new javax.swing.JTable();

        popupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                popupMenuPopupMenuWillBecomeVisible(evt);
            }
        });

        addAccount.setText("Add");
        addAccount.setToolTipText("");
        addAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountActionPerformed(evt);
            }
        });
        popupMenu.add(addAccount);

        removeAccount.setText("Delete");
        removeAccount.setToolTipText("");
        removeAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAccountActionPerformed(evt);
            }
        });
        popupMenu.add(removeAccount);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("KgaInfo bot");
        setLocationByPlatform(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Accounts"));

        tblAccounts.setModel(getAccountsTableModel());
        tblAccounts.setComponentPopupMenu(popupMenu);
        tblAccounts.setFillsViewportHeight(true);
        tblAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAccountsMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblAccounts);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Schedule"));

        jLabel1.setText("Start time");

        txtStartTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("h:mm"))));

        jLabel2.setText("End time");

        txtEndTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("h:mm"))));

        chkWeekDay1.setText("Monday");

        chkWeekDay2.setText("Tuesday");

        chkWeekDay3.setText("Wednesday");

        chkWeekDay4.setText("Thursday");

        chkWeekDay5.setText("Friday");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStartTime)
                            .addComponent(txtEndTime, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkWeekDay1)
                            .addComponent(chkWeekDay2)
                            .addComponent(chkWeekDay3)
                            .addComponent(chkWeekDay4)
                            .addComponent(chkWeekDay5))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEndTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(chkWeekDay1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkWeekDay2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkWeekDay3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkWeekDay4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkWeekDay5)
                .addGap(0, 115, Short.MAX_VALUE))
        );

        btnGetInfo.setText("Get info");
        btnGetInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetInfoActionPerformed(evt);
            }
        });

        txtGetInfoStatus.setEditable(false);
        txtGetInfoStatus.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGetInfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGetInfoStatus))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(138, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGetInfo)
                    .addComponent(txtGetInfoStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Setup", jPanel1);

        tblTasks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account", "Structure", "Department", "Theme", "Comment", "Time", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblTasks);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Tasks", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblAccountsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccountsMousePressed
		// selects the row at which point the mouse is clicked
        int currentRow = tblAccounts.rowAtPoint(evt.getPoint());
		if (currentRow >= 0)
			tblAccounts.setRowSelectionInterval(currentRow, currentRow);
    }//GEN-LAST:event_tblAccountsMousePressed

    private void addAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountActionPerformed
        ((AccountsTableModel)tblAccounts.getModel()).addRow();
    }//GEN-LAST:event_addAccountActionPerformed

    private void removeAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAccountActionPerformed
        int row = tblAccounts.getSelectedRow();
		if (row >= 0)
			((AccountsTableModel)tblAccounts.getModel()).removeRow(row);
    }//GEN-LAST:event_removeAccountActionPerformed

    private void popupMenuPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_popupMenuPopupMenuWillBecomeVisible
        int row = tblAccounts.getSelectedRow();
		removeAccount.setVisible(row >= 0);
    }//GEN-LAST:event_popupMenuPopupMenuWillBecomeVisible

    private void btnGetInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetInfoActionPerformed
		if (tblAccounts.getRowCount() <= 0) {
			JOptionPane.showMessageDialog(this, "No account yet!");
			return;
		}
		
		final Account account = ((AccountsTableModel)tblAccounts.getModel()).getAccount(tblAccounts.getSelectedRow());
		
		btnGetInfo.setEnabled(false);
		txtGetInfoStatus.setText("in progress");
	
		SwingWorker<Info, Void> worker = new SwingWorker<Info, Void>() {
		
			@Override
			public Info doInBackground() throws Exception {
				Info info = new Info(InfoFileName);
				Session session = new Session();
				
				try {
					if (session.login(account.login, account.password)) {
						List<Structure> structures = session.getStructureList();
						info.addStructures(structures);

						for (Structure s : structures) {
							List<Department> departments = session.getDepartmentList(Integer.toString(s.code));
							info.addDepartments(s.code, departments);

							for (Department d : departments) {
								List<Theme> themes = session.getThemeList(Integer.toString(d.code));
								info.addThemes(s.code, d.code, themes);
							}
						}		

						info.saveToFile();
					} else {
						resultString = "login failed";
					}
				} catch (UnirestException ex) {
					resultString = "network failed";
				}
				
				return info;
			}

			@Override
			public void done() {
				btnGetInfo.setEnabled(true);
				txtGetInfoStatus.setText(resultString);
			}
			
			private String resultString = "done";
		};
		
		worker.execute();
    }//GEN-LAST:event_btnGetInfoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGetInfo;
    private javax.swing.JCheckBox chkWeekDay1;
    private javax.swing.JCheckBox chkWeekDay2;
    private javax.swing.JCheckBox chkWeekDay3;
    private javax.swing.JCheckBox chkWeekDay4;
    private javax.swing.JCheckBox chkWeekDay5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JMenuItem removeAccount;
    private javax.swing.JTable tblAccounts;
    private javax.swing.JTable tblTasks;
    private javax.swing.JFormattedTextField txtEndTime;
    private javax.swing.JTextField txtGetInfoStatus;
    private javax.swing.JFormattedTextField txtStartTime;
    // End of variables declaration//GEN-END:variables

	private AccountsManager accountsManager;
	private Schedule schedule;
	private Info info;
}
