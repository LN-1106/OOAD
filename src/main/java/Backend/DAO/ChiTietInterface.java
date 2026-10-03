package Backend.DAO;

import java.util.ArrayList;

public interface ChiTietInterface<T> {
    public int insert(ArrayList<T> t); 
    public int delete(String id);
    public int update(ArrayList<T> t, String pk);
    public ArrayList<T> selectAll(String id);
}
