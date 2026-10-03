package Frontend.GUI.SanPham;

import Backend.BUS.SanPhamBUS;
import Backend.DTO.SanPham;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.util.ArrayList;
import net.miginfocom.swing.MigLayout;

public class SanPhamPanel extends JPanel {
    
    private SanPhamBUS spBUS = new SanPhamBUS();
    private SanPhamToolbar toolbar; 
    private SanPhamTable table;

    public SanPhamPanel() {
        setLayout(new MigLayout("fill, insets 10, gap 10", "[grow]", "[]10[grow]"));
        setBackground(new Color(237, 241, 245));
        initComponents();
        loadData(); 
    }

    private void initComponents() {
        table = new SanPhamTable();
        toolbar = new SanPhamToolbar(this);
        
        table.putClientProperty("FlatLaf.style", "arc: 15"); 
        add(toolbar, "growx, wrap");

        toolbar.putClientProperty("FlatLaf.style", "arc: 15");
        add(table, "grow"); 

        JTable tbl=table.getTable();
        tbl.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    int selectedRow=tbl.getSelectedRow();
                    if(selectedRow!=-1){
                        String maSP=tbl.getValueAt(selectedRow,1).toString();
                        String tenSP=tbl.getValueAt(selectedRow,2).toString();
                        JFrame frameCha=(JFrame) SwingUtilities.getWindowAncestor(SanPhamPanel.this);
                        PhienBanSPDialog dialog = new PhienBanSPDialog(frameCha, maSP, tenSP,false);
                        dialog.setVisible(true);
                    }
                }
            }
        });
    }
 
    public void loadData() {
        ArrayList<SanPham> list = spBUS.getAll();
        table.showData(list);
    }
    public SanPhamTable getTable(){
        return table;
    }
}