/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.buyer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author Ada
 */
public class BuyerGui extends javax.swing.JFrame {
    private BuyerAgent agent;

    /**
     * Creates new form SellerGui
     */
    public BuyerGui() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        book = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        maxPrice = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        booksTable = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setResizable(false);

        setTitle("Comprador");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                agent.doDelete();
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("Comprador");

        jLabel2.setText("Libro:");

        jLabel3.setText("Precio máximo:");

        booksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Estado", "Libro", "Precio actual", "Máximo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        booksTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                onRowClicked(evt);
            }
        });
        jScrollPane1.setViewportView(booksTable);

        jButton1.setText("Añadir interés");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddBookButtonClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(book)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(maxPrice, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(book, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(maxPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onRowClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onRowClicked
        JTable table = (JTable) evt.getSource();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        Point p = evt.getPoint();
        int row = table.rowAtPoint(p);

        if(evt.getClickCount() == 2) {
            if(tableModel.getValueAt(row, 0).equals("Esperando vendedor") || tableModel.getValueAt(row, 0).equals("En puja")) {
                String book = (String) tableModel.getValueAt(row, 1);
                int confirm = JOptionPane.showConfirmDialog(this, "Estás seguro de que deseas abandonar la puja para " + book,
                        "Abandonar la puja", JOptionPane.YES_NO_OPTION);

                if(confirm == JOptionPane.YES_OPTION) {
                    this.agent.deregisterBook(book);
                    tableModel.removeRow(row);
                }
            }
        }
    }//GEN-LAST:event_onRowClicked

    private void onAddBookButtonClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddBookButtonClicked
        this.addBook(this.book.getText(), Float.valueOf(this.maxPrice.getText()));
    }//GEN-LAST:event_onAddBookButtonClicked

    public static BuyerGui start() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BuyerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BuyerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BuyerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BuyerGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        return new BuyerGui();
    }

    public void addBook(String book, float maxPrice) {
        DefaultTableModel model = (DefaultTableModel) this.booksTable.getModel();
        model.addRow(new Object[]{"Esperando vendedor", book, null, maxPrice});

        this.agent.bidForBook(book, maxPrice);
    }

    public void updatePrice(String book, float newPrice) {
        DefaultTableModel model = (DefaultTableModel) this.booksTable.getModel();

        for(int i = 0; i < model.getRowCount(); i++) {
            if(model.getValueAt(i, 1).equals(book)) {
                model.setValueAt("En puja", i, 0);
                model.setValueAt(newPrice, i, 2);
                break;
            }
        }
    }

    public void bookPriceExceeded(String book) {
        DefaultTableModel model = (DefaultTableModel) this.booksTable.getModel();

        for(int i = 0; i < model.getRowCount(); i++) {
            if(model.getValueAt(i, 1).equals(book)) {
                model.setValueAt("Máximo excedido", i, 0);
                break;
            }
        }

        JOptionPane.showMessageDialog(this, String.format("El libro %s ha excedido tu precio máximo", book));
    }

    public void bookWon(String book, float price) {
        DefaultTableModel model = (DefaultTableModel) this.booksTable.getModel();

        for(int i = 0; i < model.getRowCount(); i++) {
            if(model.getValueAt(i, 1).equals(book)) {
                model.setValueAt("Libro ganado", i, 0);
                model.setValueAt(price, i, 2);
                break;
            }
        }

        JOptionPane.showMessageDialog(this, String.format("Enhorabuena, has conseguido el libro %s por %f euros", book, price));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField book;
    private javax.swing.JTable booksTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField maxPrice;

    public BuyerGui setAgent(BuyerAgent agent) {
        this.agent = agent;
        return this;
    }
    // End of variables declaration//GEN-END:variables
}
