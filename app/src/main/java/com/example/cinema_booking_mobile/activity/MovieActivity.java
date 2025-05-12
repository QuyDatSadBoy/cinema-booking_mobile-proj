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
import com.example.cinema_booking_mobile.dto.response.PhimDTO;
import com.example.cinema_booking_mobile.model.Phim;
import com.example.cinema_booking_mobile.service.IPhimService;
import com.example.cinema_booking_mobile.util.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private IPhimService iPhimService;
    private Phim phim;

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

        iPhimService = ApiUtils.getPhimService();
        iPhimService.getPhim(position).enqueue(new Callback<PhimDTO>() {
            @Override
            public void onResponse(Call<PhimDTO> call, Response<PhimDTO> response) {
                if (response.isSuccessful()) {
                    PhimDTO phimDTO = response.body();
                    phim = new Phim(
                            phimDTO.getId(), phimDTO.getTen(), phimDTO.getTheLoai(), phimDTO.getDoDai(),
                            phimDTO.getNgonNgu(), phimDTO.getDaoDien(),phimDTO.getDienVien(),
                            phimDTO.getMoTa(), phimDTO.getPoster(), phimDTO.getTrailer(),
                            phimDTO.getNamSx(), phimDTO.getHangSx(), phimDTO.getDoTuoi(),
                            phimDTO.getDanhGia(), phimDTO.getTrangThai());

                    WebSettings webSettings = trailer.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setAllowContentAccess(true);
                    webSettings.setAllowFileAccess(true);
                    webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    String html =
                            "<html><body style=\"margin: 0; padding: 0;\">" +
                            "<iframe width=\"100%\" height=\"100%\" " +
                            "src=\"" + phim.getTrailer() + "?rel=0&showinfo=0&autoplay=1&controls=0\" " +
                            "frameborder=\"0\" allowfullscreen></iframe>" +
                            "</body></html>";

                    trailer.setWebViewClient(new WebViewClient());
                    trailer.setWebChromeClient(new WebChromeClient());
                    trailer.loadDataWithBaseURL(
                            null, html, "text/html", "utf-8", null);

                    tenVaDanhGia.setText(phim.getTen() + " (" + phim.getDanhGia() + "/10)");
                    moTa.setText(phim.getMoTa());
                    doTuoi.setText(phim.getDoTuoi());
                    theLoai.setText(phim.getTheLoai());
                    ngonNgu.setText(phim.getNgonNgu());
                    thoiLuong.setText(phim.getDoDai() + " phút");
                    daoDien.setText(phim.getDaoDien());
                    dienVien.setText(phim.getDienVien());

                    System.out.println("Thành công: ");
                } else {
                    System.out.println("Thất bại: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PhimDTO> call, Throwable t) {
                System.out.println("Lỗi: " + t.getMessage());
            }
        });

        datVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieActivity.this, BookingTicketActivity.class);
                intent.putExtra("movieId", phim.getId());
                intent.putExtra("poster", phim.getPoster());
                startActivity(intent);
            }
        });
    }
}