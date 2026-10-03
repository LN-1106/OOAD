package Frontend.GUI.PhanQuyen;

import Backend.BUS.NhomQuyenBUS;
import Backend.DTO.NhomQuyen;
import Frontend.Compoent.Table;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PhanQuyenTable extends JPanel {
      private JTable tbl;
      private DefaultTableModel tblModel;
      private NhomQuyenBUS nqBUS = new NhomQuyenBUS();
      private TableRowSorter<DefaultTableModel> sorter;

      public PhanQuyenTable() {
            setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
            setBackground(Color.WHITE);
            initTable();
            loadData();
      }

      private void initTable() {
            String[] columns = { "STT", "Mã Nhóm Quyền", "Tên Nhóm Quyền", "Mô tả" };
            tblModel = new DefaultTableModel(columns, 0) {
                  @Override
                  public boolean isCellEditable(int row, int column) {
                        return false;
                  }
            };

            tbl = new Table();
            tbl.setModel(tblModel);
            sorter = new TableRowSorter<>(tblModel);
            tbl.setRowSorter(sorter);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tbl.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            tbl.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

            JScrollPane scrollPane = new JScrollPane(tbl);
            scrollPane.setBorder(null);
            add(scrollPane, "grow");

            tbl.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                              int row = tbl.getSelectedRow();
                              String ma = tbl.getValueAt(row, 1).toString();
                              String ten = tbl.getValueAt(row, 2).toString();

                              JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(PhanQuyenTable.this);
                              XemTaiKhoanNhomQuyenDialog dialog = new XemTaiKhoanNhomQuyenDialog(parent, ma, ten);
                              dialog.setVisible(true);
                        }
                  }
            });
      }

      public void loadData() {
            tblModel.setRowCount(0);
            ArrayList<NhomQuyen> list = nqBUS.getAll();
            int stt = 1;
            for (NhomQuyen nq : list) {
                  tblModel.addRow(new Object[] { stt++, nq.getMaNhomQuyen(), nq.getTenNhomQuyen(), nq.getMoTa() });
            }
      }

      public void loadDataBySearch(String query) {
            tblModel.setRowCount(0);
            ArrayList<NhomQuyen> list = nqBUS.getAll();
            int stt = 1;
            for (NhomQuyen nq : list) {
                  if (nq.getMaNhomQuyen().toLowerCase().contains(query)
                              || nq.getTenNhomQuyen().toLowerCase().contains(query)) {
                        tblModel.addRow(
                                    new Object[] { stt++, nq.getMaNhomQuyen(), nq.getTenNhomQuyen(), nq.getMoTa() });
                  }
            }
      }

      public JTable getTbl() {
            return tbl;
      }
}