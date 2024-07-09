import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    private static final String FILE_PATH = "users.dat";
    private Map<String, User> users;

    public DataManager() {
        users = loadUsers();
    }

    @SuppressWarnings("unchecked")
    private Map<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
