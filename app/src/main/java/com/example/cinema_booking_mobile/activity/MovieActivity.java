package com.example.cinema_booking_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.model.Phim;

public class MovieActivity extends AppCompatActivity {
    WebView trailer;
    TextView tenVaDanhGia;
    TextView moTa;
    TextView doTuoi;
    TextView theLoai;
    TextView ngonNgu;
    TextView thoiLuong;
    TextView daoDien;
    TextView dienVien;
    Button datVe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        trailer = findViewById(R.id.trailer);
        tenVaDanhGia = findViewById(R.id.tenVaDanhGia);
        moTa = findViewById(R.id.moTa);
        doTuoi = findViewById(R.id.doTuoi);
        theLoai = findViewById(R.id.theLoai);
        ngonNgu = findViewById(R.id.ngonNgu);
        thoiLuong = findViewById(R.id.thoiLuong);
        daoDien = findViewById(R.id.daoDien);
        dienVien = findViewById(R.id.dienVien);
        datVe = findViewById(R.id.datVe);

        Integer position = getIntent().getIntExtra("movieId", 0);

        // Get movie with id transferred

        Phim test = new Phim(
                1,
                "Cuon theo chieu gio",
                "Lang man",
                120,
                "Tieng Anh",
                "No info",
                "No info",
                "Cuốn theo chiều gió là một bộ phim sử thi lãng mạn lấy bối cảnh thời Nội chiến Hoa Kỳ và thời kỳ Tái thiết. Phim xoay quanh cuộc đời của Scarlett O'Hara, một tiểu thư miền Nam kiêu kỳ và mạnh mẽ, khi cô phải đối mặt với mất mát, tình yêu, chiến tranh và sự thay đổi của thời cuộc. Với mối quan hệ phức tạp giữa Scarlett và Rhett Butler, bộ phim là bản anh hùng ca về sự kiên cường, đam mê và bi kịch cá nhân giữa những biến động lịch sử.",
                String.valueOf(R.drawable.poster),
                "https://www.youtube.com/watch?v=n9xhJrPXop4",
                2023,
                "No info",
                "13+",
                8.7f,
                "Sap chieu");


        WebSettings webSettings = trailer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        String html = "<html><body style=\"margin: 0; padding: 0;\">" +
                "<iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/n9xhJrPXop4?rel=0&showinfo=0&autoplay=1&controls=0\" " +
                "frameborder=\"0\" allowfullscreen></iframe>" +
                "</body></html>";

        trailer.setWebViewClient(new WebViewClient());
        trailer.setWebChromeClient(new WebChromeClient());
        trailer.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

        tenVaDanhGia.setText(test.getTen() + " (" + test.getDanhGia() + "/10)");
        moTa.setText(test.getMoTa());
        doTuoi.setText(test.getDoTuoi());
        theLoai.setText(test.getTheLoai());
        ngonNgu.setText(test.getNgonNgu());
        thoiLuong.setText(test.getDoDai() + " phút");
        daoDien.setText(test.getDaoDien());
        dienVien.setText("Clark Gable, Vivien Leigh, Leslie Howard, Olivia de Havilland, Hattie McDaniel, Butterfly McQueen, Thomas Mitchell, Barbara O'Neil, Evelyn Keyes, Ann Rutherford, Ona Munson, Harry Davenport, Laura Hope Crews, Rand Brooks.");

        datVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieActivity.this, BookingTicketActivity.class);
                intent.putExtra("movieId", position);
                intent.putExtra("poster", test.getPoster());
                startActivity(intent);
            }
        });
    }
}