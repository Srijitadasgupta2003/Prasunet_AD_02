import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private byte[] passwordHash;
    private List<Task> tasks;

    public User(String username, String password) throws NoSuchAlgorithmException {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.tasks = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) throws NoSuchAlgorithmException {
        byte[] inputHash = hashPassword(password);
        return MessageDigest.isEqual(passwordHash, inputHash);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    private byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(password.getBytes());
    }

    @Override
    public String toString() {
        return username;
    }
}

