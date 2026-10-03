package Frontend.GUI.BanHang;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;
import Frontend.GUI.KhachHang.KhachHangAddDialog;
import Backend.BUS.KhachHangBUS;
import Backend.DTO.KhachHang;

public class ChonKhachHangDialog extends JDialog {
      private JTable tblKH;
      private DefaultTableModel modelKH;
      private String selectedMaKH = "";
      private String selectedTenKH = "";
      private KhachHangBUS khBUS = new KhachHangBUS();

      public ChonKhachHangDialog(JFrame parent) {
            super(parent, "Chọn khách hàng", true);
            setSize(700, 450);
            setLayout(new BorderLayout());
            setLocationRelativeTo(parent);

            JLabel lblTitle = new JLabel("DANH SÁCH KHÁCH HÀNG", JLabel.CENTER);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            add(lblTitle, BorderLayout.NORTH);

            String[] columns = { "Mã KH", "Tên Khách Hàng", "Số Điện Thoại", "Địa Chỉ" };
            modelKH = new DefaultTableModel(columns, 0) {
                  @Override
                  public boolean isCellEditable(int row, int column) {
                        return false;
                  }
            };
            tblKH = new JTable(modelKH);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tblKH.setDefaultRenderer(Object.class, centerRenderer);

            tblKH.setRowHeight(30);
            add(new JScrollPane(tblKH), BorderLayout.CENTER);

            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            CustomButton btnThem = new CustomButton("THÊM MỚI", Theme.PRIMARY_COLOR);
            CustomButton btnChon = new CustomButton("CHỌN", Theme.ACCENT_COLOR);

            btnThem.addActionListener(e -> {
                  KhachHangAddDialog addDialog = new KhachHangAddDialog();
                  addDialog.setVisible(true);

                  loadData();
            });

            btnChon.addActionListener(e -> {
                  int row = tblKH.getSelectedRow();
                  if (row != -1) {
                        selectedMaKH = modelKH.getValueAt(row, 0).toString();
                        selectedTenKH = modelKH.getValueAt(row, 1).toString();
                        dispose();
                  } else {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn một khách hàng!");
                  }
            });

            tblKH.addMouseListener(new java.awt.event.MouseAdapter() {
                  @Override
                  public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (e.getClickCount() == 2) {
                              int row = tblKH.getSelectedRow();
                              if (row != -1) {
                                    selectedMaKH = modelKH.getValueAt(row, 0).toString();
                                    selectedTenKH = modelKH.getValueAt(row, 1).toString();
                                    dispose();
                              }
                        }
                  }
            });

            pnlButtons.add(btnThem);
            pnlButtons.add(btnChon);
            add(pnlButtons, BorderLayout.SOUTH);

            loadData();
      }

      private void loadData() {
            modelKH.setRowCount(0);
            try {
                  ArrayList<KhachHang> list = khBUS.getAllKhachHang();
                  if (list != null) {
                        for (KhachHang kh : list) {
                              modelKH.addRow(new Object[] {
                                          kh.getId(),
                                          kh.getHoTen(),
                                          kh.getSdt(),
                                          kh.getDiaChi()
                              });
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

      public class SelectedKH {
            public String maKH, tenKH;

            public SelectedKH(String ma, String ten) {
                  this.maKH = ma;
                  this.tenKH = ten;
            }
      }

      public SelectedKH getSelectedKH() {
            return selectedMaKH.isEmpty() ? null : new SelectedKH(selectedMaKH, selectedTenKH);
      }

}
