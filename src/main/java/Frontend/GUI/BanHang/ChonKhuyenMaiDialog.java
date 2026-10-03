package Frontend.GUI.BanHang;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Theme;
import Backend.BUS.KhuyenMaiBUS;
import Backend.DTO.KhuyenMai;

public class ChonKhuyenMaiDialog extends JDialog {
      private JTable tblKM;
      private DefaultTableModel modelKM;
      private KhuyenMai selectedKM = null;
      private KhuyenMaiBUS kmBUS = new KhuyenMaiBUS();

      public ChonKhuyenMaiDialog(JFrame parent) {
            super(parent, "Chọn chương trình khuyến mãi", true);
            setSize(700, 400);
            setLayout(new BorderLayout());
            setLocationRelativeTo(parent);

            JLabel lblTitle = new JLabel("DANH SÁCH KHUYẾN MÃI ĐANG ÁP DỤNG", JLabel.CENTER);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            add(lblTitle, BorderLayout.NORTH);

            String[] columns = { "Mã KM", "Tên Chương Trình", "% Giảm", "Ngày Kết Thúc" };
            modelKM = new DefaultTableModel(columns, 0) {
                  @Override
                  public boolean isCellEditable(int row, int column) {
                        return false;
                  }
            };
            tblKM = new JTable(modelKM);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            for (int i : new int[] { 0, 2, 3 }) {
                  tblKM.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            tblKM.setRowHeight(35);
            add(new JScrollPane(tblKM), BorderLayout.CENTER);

            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
            pnlButtons.setBackground(Color.WHITE);

            CustomButton btnHuy = new CustomButton("ĐÓNG", new Color(149, 165, 166));
            CustomButton btnChon = new CustomButton("XÁC NHẬN CHỌN", Theme.ACCENT_COLOR);

            btnHuy.addActionListener(e -> dispose());
            btnChon.addActionListener(e -> handleSelectAction());

            tblKM.addMouseListener(new java.awt.event.MouseAdapter() {
                  @Override
                  public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (e.getClickCount() == 2)
                              handleSelectAction();
                  }
            });

            pnlButtons.add(btnHuy);
            pnlButtons.add(btnChon);
            add(pnlButtons, BorderLayout.SOUTH);

            loadData();
      }

      private void handleSelectAction() {
            int row = tblKM.getSelectedRow();
            if (row != -1) {
                  String maKM = modelKM.getValueAt(row, 0).toString();
                  this.selectedKM = kmBUS.getById(maKM);
                  dispose();
            } else {
                  JOptionPane.showMessageDialog(this, "Vui lòng chọn một mã khuyến mãi từ danh sách!");
            }
      }

      String[] columns = { "Mã KM", "Tên Chương Trình", "% Giảm", "Ngày Kết Thúc" };

      private void loadData() {
            modelKM.setRowCount(0);
            try {
                  ArrayList<KhuyenMai> list = kmBUS.getAllKhuyenMai();
                  java.util.Date today = new java.util.Date();

                  if (list != null) {
                        for (KhuyenMai km : list) {
                              boolean isActive = km.getTrangThai() == 1;
                              boolean chuaHetHan = km.getNgayKT() == null || !km.getNgayKT().before(today);
                              boolean daBatDau = km.getNgayBD() == null || !km.getNgayBD().after(today);

                              if (isActive && chuaHetHan && daBatDau) {
                                    modelKM.addRow(new Object[] {
                                                km.getMaKM(),
                                                km.getTenKM(),
                                                km.getPhanTramGiam() + "%",
                                                km.getNgayKT()
                                    });
                              }
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

      public KhuyenMai getSelectedKM() {
            return selectedKM;
      }
}