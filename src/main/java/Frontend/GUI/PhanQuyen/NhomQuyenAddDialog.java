package Frontend.GUI.PhanQuyen;

import Backend.BUS.NhomQuyenBUS;
import Backend.BUS.ChucNangBUS;
import Backend.BUS.NhanVienBUS;
import Backend.BUS.TaiKhoanBUS;
import Backend.DTO.NhomQuyen;
import Backend.DTO.ChucNang;
import Backend.DTO.ChiTietQuyen;
import Backend.DTO.NhanVien;
import Backend.DTO.ConNguoi;
import Backend.DTO.TaiKhoan;
import Frontend.Compoent.CustomButton;
import Frontend.Compoent.Table;
import Frontend.Compoent.Theme;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class NhomQuyenAddDialog extends JDialog {
      private JTextField txtTenNhom, txtHoTen, txtSDT, txtDiaChi, txtLuong, txtEmail, txtChucVu;
      private JTextField txtTenDangNhap, txtMatKhau;
      private JComboBox<String> cboNhomQuyen;
      private JTable tblQuyen;
      private DefaultTableModel model;
      private NhomQuyenBUS nqBUS = new NhomQuyenBUS();
      private ChucNangBUS cnBUS = new ChucNangBUS();
      private NhanVienBUS nvBUS = new NhanVienBUS();
      private TaiKhoanBUS tkBUS = new TaiKhoanBUS();
      private ArrayList<ChucNang> dsChucNang;
      private ArrayList<NhomQuyen> dsNhomQuyen;
      private PhanQuyenTable table;

      public NhomQuyenAddDialog(PhanQuyenTable table) {
            this.table = table;
            setTitle("Thêm nhân viên & phân quyền");
            setModal(true);
            setSize(950, 780);
            setLocationRelativeTo(null);
            setLayout(new MigLayout("fill, insets 20", "[grow]", "[]10[]10[grow]10[]"));
            getContentPane().setBackground(Color.WHITE);

            initForm();
            initTable();
            initButtons();
      }

      private void initForm() {
            JLabel lblInfo = new JLabel("THÔNG TIN NHÂN VIÊN");
            lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblInfo.setForeground(Theme.PRIMARY_COLOR);
            add(lblInfo, "wrap, gapbottom 5");

            JPanel pnlForm = new JPanel(new MigLayout("fillx, wrap 4", "[100!][grow][100!][grow]", "[]10[]10[]10[]"));
            pnlForm.setBackground(Color.WHITE);
            pnlForm.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

            pnlForm.add(new JLabel("Họ tên:"));
            txtHoTen = new JTextField();
            txtHoTen.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                  public void insertUpdate(javax.swing.event.DocumentEvent e) {
                        autoGenUsername();
                  }

                  public void removeUpdate(javax.swing.event.DocumentEvent e) {
                        autoGenUsername();
                  }

                  public void changedUpdate(javax.swing.event.DocumentEvent e) {
                  }
            });
            pnlForm.add(txtHoTen, "growx, h 35!");

            pnlForm.add(new JLabel("Chức vụ:"));
            txtChucVu = new JTextField("Nhân viên bán hàng");
            pnlForm.add(txtChucVu, "growx, h 35!");

            pnlForm.add(new JLabel("Số điện thoại:"));
            txtSDT = new JTextField();
            pnlForm.add(txtSDT, "growx, h 35!");

            pnlForm.add(new JLabel("Email:"));
            txtEmail = new JTextField();
            pnlForm.add(txtEmail, "growx, h 35!");

            pnlForm.add(new JLabel("Địa chỉ:"));
            txtDiaChi = new JTextField();
            pnlForm.add(txtDiaChi, "growx, h 35!");

            pnlForm.add(new JLabel("Lương:"));
            txtLuong = new JTextField("10000000");
            pnlForm.add(txtLuong, "growx, h 35!");

            pnlForm.add(new JLabel("Tên đăng nhập:"));
            txtTenDangNhap = new JTextField();
            txtTenDangNhap.setEditable(false);
            txtTenDangNhap.setBackground(new Color(240, 240, 240));
            pnlForm.add(txtTenDangNhap, "growx, h 35!");

            pnlForm.add(new JLabel("Mật khẩu:"));
            txtMatKhau = new JTextField("123456");
            txtMatKhau.setEditable(false);
            txtMatKhau.setBackground(new Color(240, 240, 240));
            pnlForm.add(txtMatKhau, "growx, h 35!");

            pnlForm.add(new JLabel("Nhóm quyền:"));
            dsNhomQuyen = nqBUS.getAll();
            String[] tenNhom = dsNhomQuyen.stream().map(NhomQuyen::getTenNhomQuyen).toArray(String[]::new);
            cboNhomQuyen = new JComboBox<>(tenNhom);
            cboNhomQuyen.addActionListener(e -> loadQuyenTheoNhom());
            pnlForm.add(cboNhomQuyen, "growx, h 35!, span 3");

            add(pnlForm, "growx, wrap");
      }

      private void initTable() {
            JLabel lblQuyen = new JLabel("BẢNG PHÂN QUYỀN");
            lblQuyen.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblQuyen.setForeground(Theme.PRIMARY_COLOR);
            add(lblQuyen, "wrap, gapbottom 5");

            String[] cols = { "Danh mục chức năng", "Truy cập" };
            model = new DefaultTableModel(cols, 0) {
                  @Override
                  public Class<?> getColumnClass(int col) {
                        return col == 0 ? String.class : Boolean.class;
                  }

                  @Override
                  public boolean isCellEditable(int row, int col) {
                        return col != 0;
                  }
            };

            tblQuyen = new Table();
            tblQuyen.setModel(model);
            tblQuyen.setRowHeight(40);
            loadQuyenTheoNhom();

            JScrollPane scroll = new JScrollPane(tblQuyen);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
            add(scroll, "grow, wrap");
      }

      private void initButtons() {
            JPanel pnlBtn = new JPanel(new MigLayout("fillx", "[grow][]10[]"));
            pnlBtn.setBackground(Color.WHITE);

            CustomButton btnCancel = new CustomButton("Huỷ bỏ", new Color(217, 83, 79));
            CustomButton btnSave = new CustomButton("THÊM MỚI", Theme.PRIMARY_COLOR);

            btnCancel.addActionListener(e -> dispose());
            btnSave.addActionListener(e -> handleSave());

            pnlBtn.add(new JLabel(), "growx");
            pnlBtn.add(btnCancel, "w 150!, h 40!");
            pnlBtn.add(btnSave, "w 150!, h 40!");
            add(pnlBtn, "growx");
      }

      private void loadQuyenTheoNhom() {
            model.setRowCount(0);
            dsChucNang = cnBUS.getAll();

            int selectedIndex = cboNhomQuyen.getSelectedIndex();
            if (selectedIndex < 0 || dsNhomQuyen == null)
                  return;

            String maNQ = dsNhomQuyen.get(selectedIndex).getMaNhomQuyen();
            ArrayList<ChiTietQuyen> dsQuyen = nqBUS.getQuyenByNhom(maNQ);

            for (ChucNang cn : dsChucNang) {
                  boolean coQuyen = dsQuyen.stream()
                              .anyMatch(ctq -> ctq.getMaChucNang().equals(cn.getMaChucNang()));
                  model.addRow(new Object[] {
                              "Quản lý " + cn.getTenChucNang().toLowerCase(),
                              coQuyen
                  });
            }
      }

      private void autoGenUsername() {
            String hoTen = txtHoTen.getText().trim();
            if (hoTen.isEmpty()) {
                  txtTenDangNhap.setText("");
                  return;
            }
            String normalized = Normalizer.normalize(hoTen.toLowerCase(), Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String username = pattern.matcher(normalized).replaceAll("")
                        .replaceAll("đ", "d")
                        .replaceAll("[^a-z0-9]", "");
            txtTenDangNhap.setText(username);
      }

      private void handleSave() {
            String hoTen = txtHoTen.getText().trim();
            String sdt = txtSDT.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String email = txtEmail.getText().trim();
            String chucVu = txtChucVu.getText().trim();
            String tenDN = txtTenDangNhap.getText().trim();

            if (tenDN.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Tên đăng nhập không được để trống!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  return;
            }

            if (tkBUS.isTenDangNhapTonTai(tenDN)) {
                  JOptionPane.showMessageDialog(this,
                              "Tên đăng nhập '" + tenDN
                                          + "' đã tồn tại!\nVui lòng nhập họ tên khác hoặc chỉnh sửa thủ công.",
                              "Lỗi trùng tên đăng nhập", JOptionPane.WARNING_MESSAGE);
                  txtHoTen.requestFocus();
                  return;
            }

            if (hoTen.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                  txtHoTen.requestFocus();
                  return;
            }
            if (!hoTen.matches("^[\\p{L} ]+$")) {
                  JOptionPane.showMessageDialog(this, "Họ tên chỉ được chứa chữ cái và khoảng trắng!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  txtHoTen.requestFocus();
                  return;
            }
            if (sdt.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  txtSDT.requestFocus();
                  return;
            }
            if (!sdt.matches("^0\\d{9}$")) {
                  JOptionPane.showMessageDialog(this, "SĐT phải có 10 chữ số và bắt đầu bằng số 0!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  txtSDT.requestFocus();
                  return;
            }
            if (diaChi.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                  txtDiaChi.requestFocus();
                  return;
            }
            if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
                  JOptionPane.showMessageDialog(this, "Email không đúng định dạng (ví dụ: abc@gmail.com)!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  txtEmail.requestFocus();
                  return;
            }
            if (chucVu.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Vui lòng nhập chức vụ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                  txtChucVu.requestFocus();
                  return;
            }
            if (chucVu.matches(".*\\d.*")) {
                  JOptionPane.showMessageDialog(this, "Chức vụ không được chứa chữ số!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  txtChucVu.requestFocus();
                  return;
            }

            double luong = 0;
            try {
                  luong = Double.parseDouble(txtLuong.getText().trim().replaceAll("[^0-9]", ""));
                  if (luong < 0) {
                        JOptionPane.showMessageDialog(this, "Lương phải là số dương!", "Lỗi",
                                    JOptionPane.WARNING_MESSAGE);
                        txtLuong.requestFocus();
                        return;
                  }
            } catch (NumberFormatException e) {
                  JOptionPane.showMessageDialog(this, "Lương không hợp lệ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                  txtLuong.requestFocus();
                  return;
            }

            if (tenDN.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Tên đăng nhập không được để trống!", "Lỗi",
                              JOptionPane.WARNING_MESSAGE);
                  return;
            }

            int selectedIndex = cboNhomQuyen.getSelectedIndex();
            String maNQ = dsNhomQuyen.get(selectedIndex).getMaNhomQuyen();
            String maNV = nvBUS.getNextMa();

            NhanVien nv = new NhanVien(maNV, hoTen, sdt, diaChi, chucVu, email, luong, true);

            TaiKhoan tk = new TaiKhoan();
            tk.setUsername(tenDN);
            tk.setPassword("123456");
            tk.setMaNV(maNV);
            tk.setMaNhomQuyen(maNQ);
            tk.setStatus(1);

            ArrayList<ChiTietQuyen> dsQuyen = new ArrayList<>();
            String[] hanhDongs = { "read", "create", "update", "delete" };
            for (int i = 0; i < model.getRowCount(); i++) {
                  String maCN = dsChucNang.get(i).getMaChucNang();
                  Boolean coQuyen = (Boolean) model.getValueAt(i, 1);
                  if (coQuyen != null && coQuyen) {
                        for (String hd : hanhDongs) {
                              dsQuyen.add(new ChiTietQuyen(maNQ, maCN, hd));
                        }
                  }
            }

            if (nvBUS.addNhanVienWithAccount(nv, tk)) {
                  JOptionPane.showMessageDialog(this,
                              "Thêm thành công!\nTên đăng nhập: " + tenDN + "\nMật khẩu: 123456");
                  if (table != null)
                        table.loadData();
                  dispose();
            } else {
                  JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
      }
}