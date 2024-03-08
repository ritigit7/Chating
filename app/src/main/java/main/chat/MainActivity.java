package main.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText messageEditText;
    private Button sendButton;
    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        view = findViewById(R.id.textView);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();

                if (!message.isEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                Socket socket = new Socket("192.168.1.42", 5000); // Replace with your laptop's server port

                                PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
                                outToServer.println(message);
                                final String finalMessage = message + "\n";
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView textView = findViewById(R.id.textView);
                                        textView.append(finalMessage);
                                        ScrollView scrollView = findViewById(R.id.scrollView);
                                        scrollView.fullScroll(View.FOCUS_DOWN);
                                    }
                                });

//                                socket.close();

                            } catch (IOException e) {
                                e.printStackTrace();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "Error sending message!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                    messageEditText.setText("");
                }
            }
        });
    }
}