
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountServiceImpl implements AccountService {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public User getCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? new User(currentUser.getUid()) : null;
    }

    @Override
    public String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }

    @Override
    public boolean hasUser() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void signIn(String email, String password) throws Exception {
        Tasks.await(firebaseAuth.signInWithEmailAndPassword(email, password));
    }

    @Override
    public void signUp(String email, String password) throws Exception {
        Tasks.await(firebaseAuth.createUserWithEmailAndPassword(email, password));
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    @Override
    public void deleteAccount() throws Exception {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Tasks.await(user.delete());
        }
    }
}
