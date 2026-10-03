package Frontend.GUI.SanPham;

import Backend.BUS.PhienBanSanPhamBUS;
import Backend.DTO.PhienBanSanPham;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.nio.file.Files;
//import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PhienBanSPDialog extends JDialog {
        private JTable tblPB;
        private DefaultTableModel model;
        private PhienBanSanPhamBUS pbBUS = new PhienBanSanPhamBUS();
        private String maSPHT, tenSPHT;
        private boolean isEditMode;
        private JLabel lblImage;
        private ArrayList<PhienBanSanPham> listPhienBan;

        public PhienBanSPDialog(JFrame parent, String maSP, String tenSP, boolean isEditMode) {
                super(parent, isEditMode ? "Danh sách phiên bản - " + tenSP : "Danh sách phiên bản - " + tenSP, true);
                this.maSPHT = maSP;
                this.tenSPHT = tenSP;
                this.isEditMode = isEditMode;

                setSize(1000, 550);
                setLocationRelativeTo(null);
                setLayout(new BorderLayout());
                getContentPane().setBackground(Color.WHITE);

                initComponents();
                loadData();
        }

        public void initComponents() {
                JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
                pnlHeader.setBackground(Theme.PRIMARY_COLOR);
                JLabel lblHeader = new JLabel(isEditMode ? "DANH SÁCH PHIÊN BẢN - " + tenSPHT.toUpperCase()
                                : "DANH SÁCH PHIÊN BẢN - " + tenSPHT.toUpperCase());
                lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
                lblHeader.setForeground(Color.WHITE);
                pnlHeader.add(lblHeader);
                add(pnlHeader, BorderLayout.NORTH);

                JPanel pnlMain = new JPanel(new MigLayout("fill, insets 15", "[grow]15[280!]", "[grow]"));
                pnlMain.setBackground(Color.WHITE);

                String[] columns = { "Mã PB", "Màu Sắc", "Công Suất", "Pin", "Kết Nối", "Giá Nhập", "Giá Bán",
                                "Tồn Kho" };
                model = new DefaultTableModel(columns, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };
                tblPB = new Table();
                tblPB.setModel(model);

                JScrollPane scroll = new JScrollPane(tblPB);
                scroll.getViewport().setBackground(Color.WHITE);
                pnlMain.add(scroll, "grow");

                JPanel pnlPreview = new JPanel(new MigLayout("wrap 1, fill, insets 15", "[center]", "[center]"));
                pnlPreview.setBackground(new Color(248, 249, 250));
                pnlPreview.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

                lblImage = new JLabel("Chưa có hình ảnh", SwingConstants.CENTER);
                lblImage.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
                pnlPreview.add(lblImage, "w 250!, h 250!");

                if (isEditMode) {
                        CustomButton btnChonAnh = new CustomButton("Thay đổi hình ảnh", Theme.PRIMARY_COLOR);
                        btnChonAnh.addActionListener(e -> {
                                int row = tblPB.getSelectedRow();
                                if (row == -1) {
                                        JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiên bản trên bảng trước!");
                                        return;
                                }
                                xuLyChonAnh(row);
                        });
                        pnlPreview.add(btnChonAnh, "w 170!, h 35!");
                }

                pnlMain.add(pnlPreview, "growy");
                add(pnlMain, BorderLayout.CENTER);

                tblPB.getSelectionModel().addListSelectionListener(e -> {
                        if (!e.getValueIsAdjusting()) {
                                int row = tblPB.getSelectedRow();
                                if (row != -1) {
                                        PhienBanSanPham pb = listPhienBan.get(row);
                                        if (pb.getHinhAnh() != null && !pb.getHinhAnh().isEmpty()) {
                                                loadProductImage(pb.getHinhAnh());
                                        } else {
                                                lblImage.setIcon(null);
                                                lblImage.setText("Không có ảnh");
                                        }
                                }
                        }
                });

                tblPB.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                                if (e.getClickCount() == 2) {
                                        int row = tblPB.getSelectedRow();
                                        if (row != -1) {
                                                String maPB = tblPB.getValueAt(row, 0).toString();
                                                JFrame frameCha = (JFrame) SwingUtilities
                                                                .getWindowAncestor(PhienBanSPDialog.this);
                                                ChiTietSPDialog dialog = new ChiTietSPDialog(frameCha, maPB,
                                                                isEditMode);
                                                dialog.setVisible(true);

                                                if (isEditMode) {
                                                        loadData();
                                                }
                                        }
                                }
                        }
                });

                JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                pnlFooter.setBackground(Color.WHITE);

                if (isEditMode) {
                        CustomButton btnThem = new CustomButton("Thêm", Theme.ACCENT_COLOR);
                        CustomButton btnSua = new CustomButton("Sửa", Theme.WARNING_COLOR);
                        CustomButton btnSCT = new CustomButton("Sửa Chi Tiết", Theme.WARNING_COLOR);
                        CustomButton btnXoa = new CustomButton("Xóa", Theme.DANGER_COLOR);

                        btnThem.setPreferredSize(new Dimension(80, 40));
                        btnSua.setPreferredSize(new Dimension(80, 40));
                        btnSCT.setPreferredSize(new Dimension(130, 40));
                        btnXoa.setPreferredSize(new Dimension(80, 40));

                        pnlFooter.add(btnThem);
                        pnlFooter.add(btnSua);
                        pnlFooter.add(btnXoa);
                        pnlFooter.add(btnSCT);

                        addEditEvents(btnThem, btnSua, btnSCT, btnXoa);
                }

                // CustomButton btnDong = new CustomButton("Thoát", new Color(149, 165, 166));
                // btnDong.setPreferredSize(new Dimension(80, 40));
                // btnDong.addActionListener(e -> dispose());
                // pnlFooter.add(btnDong);
                add(pnlFooter, BorderLayout.SOUTH);
        }

        private void addEditEvents(JButton btnThem, JButton btnSua, JButton btnSCT, JButton btnXoa) {
                btnThem.addActionListener(e -> {
                        InputPhienBanDialog inputDlg = new InputPhienBanDialog(
                                        (JFrame) SwingUtilities.getWindowAncestor(this), maSPHT, null);
                        inputDlg.setVisible(true);
                        if (inputDlg.isSuccess())
                                loadData();
                });

                btnSua.addActionListener(e -> {
                        int row = tblPB.getSelectedRow();
                        if (row == -1) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiên bản để sửa!", "Thông báo",
                                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }

                        PhienBanSanPham pb = listPhienBan.get(row);
                        InputPhienBanDialog inputDlg = new InputPhienBanDialog(
                                        (JFrame) SwingUtilities.getWindowAncestor(this), maSPHT, pb);
                        inputDlg.setVisible(true);
                        if (inputDlg.isSuccess()) {
                                loadData();
                        }
                });

                btnSCT.addActionListener(e -> {
                        int row = tblPB.getSelectedRow();
                        if (row == -1) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiên bản để quản lý");
                                return;
                        }
                        String maPB = tblPB.getValueAt(row, 0).toString();
                        ChiTietSPDialog dialog = new ChiTietSPDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                                        maPB, true);
                        dialog.setVisible(true);
                        loadData();
                });

                btnXoa.addActionListener(e -> {
                        int row = tblPB.getSelectedRow();
                        if (row == -1) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiên bản để xóa", "Thông báo",
                                                JOptionPane.WARNING_MESSAGE);
                                return;
                        }
                        String maPB = tblPB.getValueAt(row, 0).toString();
                        int op = JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa phiên bản " + maPB + " không?\n",
                                        "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (op == JOptionPane.YES_OPTION) {
                                if (pbBUS.delete(maPB)) {
                                        loadData();
                                } else
                                        JOptionPane.showMessageDialog(this, "Xóa thất bại");
                        }
                });
        }

        private void loadData() {
                listPhienBan = pbBUS.getByMaSP(maSPHT);
                model.setRowCount(0);
                DecimalFormat formatter = new DecimalFormat("###,###");

                for (PhienBanSanPham pb : listPhienBan) {
                        model.addRow(new Object[] {
                                        pb.getMaPhienBan(), pb.getMauSac(), pb.getCongSuat(), pb.getPin(),
                                        pb.getKetNoi(), formatter.format(pb.getGiaNhap()),
                                        formatter.format(pb.getGiaBan()), pb.getSoLuongTon()
                        });
                }
        }

        private void loadProductImage(String imgName) {
                try {
                        java.net.URL imgURL = getClass().getClassLoader().getResource("images/product/" + imgName);
                        if (imgURL != null) {
                                ImageIcon icon = new ImageIcon(imgURL);
                                Image scaled = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                                lblImage.setIcon(new ImageIcon(scaled));
                                lblImage.setText("");
                        } else {
                                lblImage.setIcon(null);
                                lblImage.setText("Không tìm thấy ảnh");
                        }
                } catch (Exception e) {
                        lblImage.setIcon(null);
                        lblImage.setText("Lỗi tải ảnh");
                }
        }

        private void xuLyChonAnh(int row) {
                JFileChooser file = new JFileChooser();
                file.setDialogTitle("Chọn hình ảnh từ máy tính");
                file.setFileFilter(new FileNameExtensionFilter("Hình ảnh (jpg, png)", "jpg", "png", "jpeg"));
                if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = file.getSelectedFile();
                        String fileName = selectedFile.getName();
                        try {
                                File destFolder = new File("src/main/resources/images/product/");
                                if (!destFolder.exists()) destFolder.mkdirs();
                                File destFile = new File(destFolder, fileName);
                                Files.copy(selectedFile.toPath(), destFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

                                PhienBanSanPham pb = listPhienBan.get(row);
                                pb.setHinhAnh(fileName);

                                if (pbBUS.update(pb)) {
                                        loadProductImage(fileName);
                                        JOptionPane.showMessageDialog(this, "Đã lưu ảnh thành công: " + fileName);
                                }
                        } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(this, "Lỗi khi lưu ảnh: " + ex.getMessage());
                        }
                }
                
        }

        
}