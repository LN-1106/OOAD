package Frontend.GUI.SanPham;

import Backend.BUS.ChiTietSPBUS;
import Backend.DTO.ChiTietSP;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ChiTietSPDialog extends JDialog { 

        private JTable tblCT; 
        private DefaultTableModel model;
        private ChiTietSPBUS ctBUS = new ChiTietSPBUS();
        private String maPBHT;
        private boolean isEditMode;

        public ChiTietSPDialog(JFrame parent, String maPB, boolean isEditMode) {
                super(parent, isEditMode ? "SỬA CHI TIẾT - " + maPB : "DANH SÁCH CHI TIẾT - " + maPB, true);
                this.maPBHT = maPB;
                this.isEditMode = isEditMode;

                setSize(900, 500);
                setLocationRelativeTo(null);
                setLayout(new BorderLayout());
                getContentPane().setBackground(Color.WHITE);

                initComponents();
                loadData();
        }

        public void initComponents() {

                JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
                pnlHeader.setBackground(Theme.PRIMARY_COLOR);
                JLabel lblHeader = new JLabel(isEditMode ? "SỬA CHI TIẾT - " + maPBHT : "DANH SÁCH CHI TIẾT - " + maPBHT);
                lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblHeader.setForeground(Color.WHITE);
                pnlHeader.add(lblHeader);
                add(pnlHeader, BorderLayout.NORTH);

                String[] columns = { "Mã IMEI", "Mã Phiên Bản", "Mã Phiếu Nhập", "Mã Phiếu Xuất", "Tình Trạng" };
                model = new DefaultTableModel(columns, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblCT = new Table();  
                tblCT.setModel(model);

                JScrollPane scroll = new JScrollPane(tblCT);
                scroll.getViewport().setBackground(Color.WHITE);
                scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                add(scroll, BorderLayout.CENTER);

                JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                pnlFooter.setBackground(Color.WHITE);

                if (isEditMode) {
                        CustomButton btnSua = new CustomButton("Sửa", Theme.WARNING_COLOR);
                        btnSua.setPreferredSize(new Dimension(80, 40));
                        pnlFooter.add(btnSua);

                        addEditEvents(btnSua); 
                }

                // CustomButton btnDong = new CustomButton("Thoát", new Color(149, 165, 166));
                // btnDong.setPreferredSize(new Dimension(80, 40));
                // btnDong.addActionListener(e -> dispose());
                // pnlFooter.add(btnDong);
                
                add(pnlFooter, BorderLayout.SOUTH);
        }

        private void addEditEvents(JButton btnSua) {
                btnSua.addActionListener(e -> {
                        int row = tblCT.getSelectedRow();
                        if (row == -1) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng IMEI để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        String tinhTrang = tblCT.getValueAt(row, 4).toString();
                        if (tinhTrang.equalsIgnoreCase("Đã bán")) {
                            JOptionPane.showMessageDialog(this, 
                                "Máy này đã xuất bán cho khách, KHÔNG ĐƯỢC PHÉP sửa thông tin!", 
                                "Cảnh báo bảo mật", JOptionPane.ERROR_MESSAGE);
                            return; 
                        }

                        ChiTietSP ct = new ChiTietSP();
                        ct.setMaImei(tblCT.getValueAt(row, 0).toString());
                        ct.setMaPhienBan(tblCT.getValueAt(row, 1).toString());
                        Object pn = tblCT.getValueAt(row, 2);
                        Object px = tblCT.getValueAt(row, 3);
                        ct.setMaPhieuNhap(pn != null ? pn.toString() : null);
                        ct.setMaPhieuXuat(px != null ? px.toString() : null);
                        ct.setTinhTrang(tinhTrang);

                        InputChiTietDialog inputDlg = new InputChiTietDialog((JFrame) SwingUtilities.getWindowAncestor(this), maPBHT, ct);
                        inputDlg.setVisible(true);
                        
                        if (inputDlg.isSuccess()) {
                                loadData();
                        }
                });
        }

        private void loadData() {
                ArrayList<ChiTietSP> list = ctBUS.getByMaPB(maPBHT);
                model.setRowCount(0);
                for (ChiTietSP ct : list) {
                        model.addRow(new Object[] {
                                ct.getMaImei(),ct.getMaPhienBan(),ct.getMaPhieuNhap(),ct.getMaPhieuXuat(),ct.getTinhTrang()
                        });
                }
        }
}