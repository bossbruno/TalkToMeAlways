package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.TheDen.TalkToMe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.UserAdapter;
import Model.Users;

public class UsersFragment extends Fragment {

  private RecyclerView recyclerView;
  private UserAdapter userAdapter;
  private List<Users> mUsers;
  EditText search_users;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
   // Inflate the layout for this fragment
   View view = inflater.inflate(R.layout.fragment_users, container,false);
   recyclerView = view.findViewById(R.id.recycler_view);
   recyclerView.setHasFixedSize(true);
   recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

   mUsers = new ArrayList<>();
   readUsers();
   search_users = view.findViewById(R.id.search_users);
   search_users.addTextChangedListener(new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
searchUsers(s.toString().toLowerCase());

}


       @Override
       public void afterTextChanged(Editable s) {

       }
   });



   return view;
  }

    private void searchUsers(String S) {
      FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(S)
                .endAt(S+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);

                    assert users != null;
                    assert fuser != null;
                    if(!users.getId().equals(fuser.getUid())){
                        mUsers.add(users);
                    }
                    else {

                            Toast.makeText(getContext(), "No User found", Toast.LENGTH_LONG).show();
                    }
                    }

                userAdapter= new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readUsers() {
   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      assert firebaseUser != null;
   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
   reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (search_users.getText().toString().equals("")) {


            mUsers.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Users user = snapshot.getValue(Users.class);
                assert user != null;
                if (!user.getId().equals(firebaseUser.getUid())) {
                    mUsers.add(user);
                }
            }

            userAdapter = new UserAdapter(getContext(), mUsers, false);
            recyclerView.setAdapter(userAdapter);
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
   });
   }
}
