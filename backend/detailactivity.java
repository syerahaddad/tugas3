detailactivity

package com.example.kost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // untuk mendapatkan data yang dikirim dari PemesananActivity
        String email = getIntent().getStringExtra("EMAIL");
        String roomType = getIntent().getStringExtra("ROOM_TYPE");
        String tanggal = getIntent().getStringExtra("TANGGAL");
        String totalTagihan = getIntent().getStringExtra("TOTAL_TAGIHAN");

        // untuk menampilkan data di TextView atau komponen UI lainnya
        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvRoomType = findViewById(R.id.tvRoomType);
        TextView tvTanggal = findViewById(R.id.tvTanggal);
        TextView tvTotalTagihan = findViewById(R.id.tvTotalTagihan);

        tvEmail.setText("Email: " + email);
        tvRoomType.setText("Room Type: " + roomType);
        tvTanggal.setText("Tanggal: " + tanggal);
        tvTotalTagihan.setText("Total Tagihan: " + totalTagihan);

        // untuk mendapatkan referensi ke tombol "Edit"
        Button btnEdit = findViewById(R.id.btnEdit);

        // untuk menambahkan onClickListener ke tombol "Edit"
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // untuk membuat Intent untuk kembali ke PemesananActivity
                Intent editIntent = new Intent(DetailActivity.this, PemesananActivity.class);

                // untuk mengirim data yang sama ke PemesananActivity untuk diedit
                editIntent.putExtra("EMAIL", email);
                editIntent.putExtra("ROOM_TYPE", roomType);
                editIntent.putExtra("TANGGAL", tanggal);
                editIntent.putExtra("TOTAL_TAGIHAN", totalTagihan);

                // untuk menjalankan intent untuk memulai PemesananActivity untuk diedit
                startActivity(editIntent);

                // untuk menutup DetailActivity agar tidak kembali ke sini saat tombol back ditekan
                finish();
            }
        });

        // untuk mendapatkan referensi ke tombol "Finish"
        Button btnFinish = findViewById(R.id.btnFinish);

        // untuk menambahkan onClickListener ke tombol "Finish"
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // untuk membuat Intent untuk kembali ke MainActivity
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);

                // untuk menjalankan intent untuk memulai MainActivity
                startActivity(intent);

                // untuk menutup DetailActivity agar tidak kembali ke sini saat tombol back ditekan
                finish();
            }
        });


    }
}

loginactivity
package com.example.kost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button btSignUp, btNext;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };
        btSignUp = (Button) findViewById(R.id.bt_signup);
        btNext = (Button) findViewById(R.id.bt_next);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp(etEmail.getText().toString(),
                        etPassword.getText().toString());
            }
        });
        // untuk menambahkkan kode untuk tombol ""Next"
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mulai  TipeActivity ketika klik tombol Next
                startActivity(new Intent(LoginActivity.this, TipeActivity.class));
            }
        });

    }

    private void signUp(final String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" +
                                task.isSuccessful());
                        /**
                         * jika sign in gagal, maka tampilkan pesan ke user. lalu jika sign
                         in sukses maka auth state listener akan dipanggil dan logic untuk
                         menghandle signed in user bisa dihandle di listener.
                         */

                        if (!task.isSuccessful()) {
                            task.getException().printStackTrace();

                            Snackbar.make(findViewById(R.id.bt_signup), "Akun Sudah Terdaftar", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(R.id.bt_signup), "Proses Pendaftaran Berhasil\n" +
                                    "Email " + email, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fStateListener != null) {
            fAuth.removeAuthStateListener(fStateListener);
        }
    }
    private void redirectToMenuActivity() {
        startActivity(new Intent(this, MenuActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
mainactivity
package com.example.kost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            redirectToMenuActivity();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        user = mAuth.getCurrentUser();
        if (user != null) {
            redirectToMenuActivity();
        }
    }

    public void login(View v){
        String emailUser = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(emailUser, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // jika login berhasil, maka arahkan ke TipeActivity
                    redirectToMenuActivity();
                } else {
                    // namin jika login gagal, tampilkan pesan error di logcat
                    Log.w("error_auth", "Error login", task.getException());
                }
            }
        });
    }

    public void toRegister(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }
    private void redirectToMenuActivity() {
        startActivity(new Intent(this, MenuActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
menuactivity

package com.example.kost;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseAuth mAuth;

        Button btnTipeKamar = findViewById(R.id.btnTipeKamar);
        Button btnOrder = findViewById(R.id.btnOrder);
        Button btnLogout = findViewById(R.id.btnLogout);


        mAuth = FirebaseAuth.getInstance();

        btnTipeKamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, TipeActivity.class));
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, PemesananActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                // untuk navigasi kembali ke halaman login atau halaman awal
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);

                // untuk memindahkan pemanggilan finish( ke dalam callback onSuccess
                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if (firebaseAuth.getCurrentUser() == null) {
                            // Pengguna sudah logout, sekarang tutup aktivitas saat ini
                            finish();
                        }
                    }
                });
            }
        });
    }
}
order

package com.example.kost;
public class Order {
    private String email;
    private String roomType;
    private String tanggal;
    private String totalTagihan;
    public Order() {
    }

    public Order(String email, String roomType, String tanggal, String totalTagihan) {
        this.email = email;
        this.roomType = roomType;
        this.tanggal = tanggal;
        this.totalTagihan = totalTagihan;
    }

    public String getEmail() {
        return email;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getTotalTagihan() {
        return totalTagihan;
    }
}

pemesananactivity
package com.example.kost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PemesananActivity extends AppCompatActivity {
    private EditText etEmail, etTanggal, etTotalTagihan;
    private Spinner spRoomType;
    private Button btnCreate, btnDelete;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // untujk inisialisasi komponen UI
        etEmail = findViewById(R.id.et_email);
        etTanggal = findViewById(R.id.etTanggal);
        etTotalTagihan = findViewById(R.id.etTotalTagihan);
        spRoomType = findViewById(R.id.spType);
        btnCreate = findViewById(R.id.btnCreate);
        btnDelete = findViewById(R.id.btnDelete);

        // untuk inisialisasi tombol back
        Button backButton = findViewById(R.id.btnBack);

        // untuk mendapatkan referensi ke tombol "Edit"
        Button btnEdit = findViewById(R.id.btnEdit);

        // untuk menambahkan listener untuk tombol back
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // untuk handle klik tombol Back
                goToMenuActivity();
            }
        });



        // untuk inisialisasi Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("order");

        // untuk mengatur Spinner dengan opsi room type
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.room_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoomType.setAdapter(adapter);

        // untuk mengatur aksi ketika item spinner dipilih
        spRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // untuk handle aksi ketika item spinner dipilih
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            };;
        });

        // untuk mengatur aksi klik tombol create
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });

        // untuk mengatur aksi klik tombol delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });
    }

    // fungsi untuk membuat order dan menyimpan ke database
    private void createOrder() {
        String email = etEmail.getText().toString().trim();
        String roomType = spRoomType.getSelectedItem().toString();
        String tanggal = etTanggal.getText().toString().trim();
        String totalTagihan = etTotalTagihan.getText().toString().trim();


        // untuk buat objek Order
        Order order = new Order(email, roomType, tanggal, totalTagihan);

        // untuk simpan order ke database
        String key = mDatabase.push().getKey();
        mDatabase.child(key).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    showToast("Order berhasil dibuat!");
                    // Kirim data ke DetailActivity
                    Intent intent = new Intent(PemesananActivity.this, DetailActivity.class);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("ROOM_TYPE", roomType);
                    intent.putExtra("TANGGAL", tanggal);
                    intent.putExtra("TOTAL_TAGIHAN", totalTagihan);
                    startActivity(intent);
                    clearFields();
                } else {
                    showToast("Gagal membuat order. Coba lagi.");
                }
            }
        });
    }

    // untuk fungsi untuk pindah ke MenuActivity
    private void goToMenuActivity() {
        // Create an Intent to start MenuActivity
        Intent intent = new Intent(PemesananActivity.this, MenuActivity.class);

        // memulai MenuActivity
        startActivity(intent);

        // menyelesaikan aktivitas saat ini (PemesananActivity)
        finish();
    }

    // fungsi untuk menghapus isi input
    private void clearFields() {
        etEmail.setText("");
        spRoomType.setSelection(0);
        etTanggal.setText("");
        etTotalTagihan.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

registeractivity

package com.example.kost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {
    private Button btSignUp;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();

        fStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };


        btSignUp = (Button) findViewById(R.id.bt_signup);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp(etEmail.getText().toString(),
                        etPassword.getText().toString());
            }
        });
    }
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private void signUp(final String email, String password){
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" +
                                task.isSuccessful());
                        /**
                         * jika sign in gagal, maka tampilkan pesan ke user. namun jika sign
                         in sukses maka auth state listener akan dipanggil dan logic untuk
                         menghandle signed in user bisa dihandle di listener.
                         */

                        if (!task.isSuccessful()) {
                            task.getException().printStackTrace();

                            Snackbar.make(findViewById(R.id.bt_signup), "Proses Pendaftaran Gagal", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(findViewById(R.id.bt_signup), "Proses Pendaftaran Berhasil\n" +
                                    "Email "+email, Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(fStateListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (fStateListener != null) {
            fAuth.removeAuthStateListener(fStateListener);
        }
    }
}

tipeactivity
package com.example.kost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class TipeActivity extends AppCompatActivity {


    private Button nextButton;
    private Spinner spRoomType;
    private ArrayAdapter<CharSequence> adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipe);

        // inisialisasi spinner dan ArrayAdapter
        spRoomType = findViewById(R.id.spType);
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.room_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoomType.setAdapter(adapter);


        // tombol inisialisasi
        nextButton = findViewById(R.id.bt_next);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SELECTED_ROOM_TYPE")) {
            String selectedRoomType = intent.getStringExtra("SELECTED_ROOM_TYPE");


            int position = adapter.getPosition(selectedRoomType);
            spRoomType.setSelection(position);
        }


        // untuk menambahkan listener untuk button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // untuk menghandle klik button Next
                goToPemesananActivity();
            }
        });
    }

    // ini method untuk pindah ke PemesananActivity
    private void goToPemesananActivity() {
        // dapatkan tipe kamar yang dipilih (dengan asumsi itu adalah string)
        String selectedRoomType = "YourLogicToGetSelectedRoomType"; // Replace this with the actual logic

        // buat intent untuk memulai PemesananActivity
        Intent intent = new Intent(TipeActivity.this, PemesananActivity.class);

        // berikan tipe kamar yang dipilih sebagai tambahan
        intent.putExtra("SELECTED_ROOM_TYPE", selectedRoomType);

        // mulai PemesananActivity
        startActivity(intent);

        // selesaikan aktivitas saat ini(TipeActivity)
        finish();
    }


    // override method onStart untuk memastikan pengguna sudah login sebelum melanjutkan
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // jika pengguna belum login, maka arahkan ke aktivitas login atau tindakan yang sesuai

            finish();
        }
    }
    public void logoutUser(View view) {
        mAuth.signOut();

        // untuk navigasi kembali ke halaman login atau halaman awal
        Intent intent = new Intent(TipeActivity.this, MainActivity.class);
        startActivity(intent);

        // untuk memindahkan pemanggilan finish() ke dalam callback onSuccess
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    finish();
                }
            }
        });
    }
}
