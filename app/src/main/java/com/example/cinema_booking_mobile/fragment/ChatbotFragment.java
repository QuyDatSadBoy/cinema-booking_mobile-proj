package com.example.cinema_booking_mobile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinema_booking_mobile.R;
import com.example.cinema_booking_mobile.adapter.ChatMessageAdapter;
import com.example.cinema_booking_mobile.adapter.ModelSpinnerAdapter;
import com.example.cinema_booking_mobile.model.ChatMessage;
import com.example.cinema_booking_mobile.model.PerplexityModel;
import com.example.cinema_booking_mobile.service.perplexity.PerplexityRequest;
import com.example.cinema_booking_mobile.service.perplexity.PerplexityResponse;
import com.example.cinema_booking_mobile.util.ApiUtils;
import com.example.cinema_booking_mobile.util.ChatbotPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotFragment extends Fragment {

    private RecyclerView rvChatMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FloatingActionButton fabSettings;
    private Spinner spinnerModel;

    private List<ChatMessage> messages = new ArrayList<>();
    private ChatMessageAdapter adapter;
    private ChatbotPreferences chatbotPreferences;
    private List<PerplexityModel> models = new ArrayList<>();
    private PerplexityModel selectedModel;

    // Key để lưu tin nhắn trong SharedPreferences
    public static final String PREF_KEY_MESSAGES = "chatbot_messages";
    // Flag để kiểm tra xem đã hiển thị tin nhắn chào chưa
    private boolean isInitialized = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        chatbotPreferences = new ChatbotPreferences(requireContext());

        initViews(view);
        setupModels();
        setupRecyclerView();
        setupModelSpinner();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        rvChatMessages = view.findViewById(R.id.rvChatMessages);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend = view.findViewById(R.id.btnSend);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        fabSettings = view.findViewById(R.id.fabSettings);
        spinnerModel = view.findViewById(R.id.spinnerModel);

        if (messages.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    private void setupModels() {
        // Đảm bảo xóa danh sách cũ trước khi thêm mới
        models.clear();

        // Khởi tạo danh sách models với mô tả tiếng Việt
        models.add(new PerplexityModel(
                "sonar-pro",
                "Sonar Pro",
                "Model Tìm kiếm",
                "Model tìm kiếm nâng cao có khả năng xử lý các truy vấn phức tạp và theo dõi cuộc trò chuyện.",
                "Phù hợp cho truy vấn nhanh về sự kiện, tóm tắt chủ đề, so sánh sản phẩm và tin tức hiện tại.",
                "Không lý tưởng cho phân tích đa bước hoặc nghiên cứu sâu về các chủ đề rộng."
        ));

        models.add(new PerplexityModel(
                "sonar",
                "Sonar",
                "Model Tìm kiếm",
                "Model tìm kiếm nhẹ, tiết kiệm chi phí với khả năng cung cấp thông tin chính xác.",
                "Phù hợp cho việc truy xuất thông tin đơn giản và tổng hợp không cần lý luận phức tạp.",
                "Không lý tưởng cho phân tích nhiều bước hoặc báo cáo toàn diện."
        ));

        models.add(new PerplexityModel(
                "sonar-deep-research",
                "Sonar Deep Research",
                "Model Nghiên cứu",
                "Model nghiên cứu chuyên sâu thực hiện tìm kiếm kỹ lưỡng và tạo báo cáo toàn diện.",
                "Lý tưởng cho báo cáo chủ đề toàn diện và phân tích sâu với nghiên cứu web kỹ lưỡng.",
                "Không nên dùng cho truy vấn nhanh hoặc công việc cần thời gian nhanh (mất 30+ phút)."
        ));

        models.add(new PerplexityModel(
                "sonar-reasoning-pro",
                "Sonar Reasoning Pro",
                "Model Lý luận",
                "Model lý luận hàng đầu được hỗ trợ bởi DeepSeek R1 với Chuỗi Suy nghĩ (CoT).",
                "Xuất sắc cho phân tích phức tạp yêu cầu tư duy từng bước và tổng hợp thông tin.",
                "Không được khuyến nghị cho truy vấn sự kiện đơn giản hoặc truy xuất thông tin cơ bản."
        ));

        models.add(new PerplexityModel(
                "sonar-reasoning",
                "Sonar Reasoning",
                "Model Lý luận",
                "Model lý luận nhanh, thời gian thực được thiết kế cho giải quyết vấn đề nhanh với tìm kiếm.",
                "Tốt cho việc giải quyết vấn đề logic đòi hỏi các khuyến nghị được thông tin đầy đủ.",
                "Không được khuyến nghị khi tốc độ được ưu tiên hơn chất lượng lý luận."
        ));

        models.add(new PerplexityModel(
                "r1-1776",
                "R1-1776",
                "Model Offline",
                "Phiên bản của DeepSeek R1 được đào tạo để cung cấp thông tin không kiểm duyệt, không thiên vị và chính xác.",
                "Hoàn hảo cho việc tạo nội dung sáng tạo và các nhiệm vụ không yêu cầu thông tin web cập nhật.",
                "Không phù hợp cho các truy vấn cần thông tin web hiện tại."
        ));

        // Mặc định chọn model đầu tiên
        selectedModel = models.get(0);
    }

    private void setupModelSpinner() {
        ModelSpinnerAdapter modelAdapter = new ModelSpinnerAdapter(requireContext(), models);
        spinnerModel.setAdapter(modelAdapter);

        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModel = models.get(position);
                // Hiển thị thông báo nhỏ khi chọn model
                Toast.makeText(requireContext(),
                        "Đã chọn model: " + selectedModel.getName(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ChatMessageAdapter(messages);
        rvChatMessages.setAdapter(adapter);
        rvChatMessages.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Chỉ thêm tin nhắn chào nếu chưa có tin nhắn nào
        if (!isInitialized && messages.isEmpty()) {
            addBotMessage("Xin chào! Tôi là trợ lý AI của " + chatbotPreferences.getCinemaName() + ". Tôi có thể giúp gì cho bạn?");
            isInitialized = true;
            saveMessages(); // Lưu tin nhắn sau khi thêm
        }

        // Nếu có tin nhắn, ẩn trạng thái trống
        if (!messages.isEmpty()) {
            tvEmptyState.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.VISIBLE);
        }
    }

    // Phương thức lưu tin nhắn vào SharedPreferences
    private void saveMessages() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("ChatbotPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(messages);
        editor.putString(PREF_KEY_MESSAGES, json);
        editor.apply();
    }

    // Phương thức tải tin nhắn từ SharedPreferences
    private void loadMessages() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("ChatbotPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString(PREF_KEY_MESSAGES, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ChatMessage>>() {}.getType();
            List<ChatMessage> savedMessages = gson.fromJson(json, type);

            if (savedMessages != null) {
                messages.clear();
                messages.addAll(savedMessages);
                isInitialized = true;
            }
        }
    }

    // Cập nhật phương thức thêm tin nhắn để lưu
    private void addUserMessage(String content) {
        ChatMessage message = new ChatMessage(content, true, System.currentTimeMillis());
        messages.add(message);
        adapter.notifyItemInserted(messages.size() - 1);
        rvChatMessages.scrollToPosition(messages.size() - 1);
        saveMessages(); // Lưu sau khi thêm tin nhắn
    }

    private void addBotMessage(String content) {
        ChatMessage message = new ChatMessage(content, false, System.currentTimeMillis());
        messages.add(message);
        adapter.notifyItemInserted(messages.size() - 1);
        rvChatMessages.scrollToPosition(messages.size() - 1);
        saveMessages(); // Lưu sau khi thêm tin nhắn
    }

    private void setupListeners() {
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });

        fabSettings.setOnClickListener(v -> {
            // Mở fragment cài đặt với cách an toàn hơn
            ChatbotSettingsFragment settingsFragment = new ChatbotSettingsFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, settingsFragment)
                    .addToBackStack("chatbot_settings")
                    .commit();
        });
    }

    private void sendMessage(String content) {
        // Thêm tin nhắn người dùng vào danh sách
        addUserMessage(content);

        // Xóa nội dung trong EditText
        etMessage.setText("");

        // Ẩn trạng thái trống
        if (tvEmptyState.getVisibility() == View.VISIBLE) {
            tvEmptyState.setVisibility(View.GONE);
        }

        // Hiển thị ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        // Tạo nội dung hệ thống cho trợ lý AI
        String systemPrompt = createSystemPrompt();
        String fullPrompt = systemPrompt + "\n\nUser question: " + content;

        // Gửi yêu cầu đến API với model đã chọn
        PerplexityRequest request = new PerplexityRequest(fullPrompt);
        request.setModel(selectedModel.getId()); // Sử dụng model đã chọn

        ApiUtils.getPerplexityService().getCompletion(request).enqueue(new Callback<PerplexityResponse>() {
            @Override
            public void onResponse(Call<PerplexityResponse> call, Response<PerplexityResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String botReply = response.body().getCompletionText();
                    addBotMessage(botReply);
                } else {
                    addBotMessage("Xin lỗi, tôi đang gặp sự cố kết nối. Vui lòng thử lại sau.");
                }
            }

            @Override
            public void onFailure(Call<PerplexityResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                addBotMessage("Xin lỗi, tôi không thể kết nối với máy chủ lúc này. Vui lòng kiểm tra kết nối mạng của bạn.");
            }
        });
    }

    private String createSystemPrompt() {
        return "Bạn là trợ lý AI của " + chatbotPreferences.getCinemaName() +
                " tại " + chatbotPreferences.getCinemaLocation() + ".\n\n" +
                "Giới thiệu: " + chatbotPreferences.getCinemaDescription() + "\n\n" +
                "Giá vé: " + chatbotPreferences.getTicketPrices() + "\n\n" +
                "Giờ mở cửa: " + chatbotPreferences.getOpeningHours() + "\n\n" +
                "Cơ sở vật chất: " + chatbotPreferences.getFacilities() + "\n\n" +
                "Phim đang chiếu: " + chatbotPreferences.getCurrentMovies() + "\n\n" +
                "Phim sắp chiếu: " + chatbotPreferences.getUpcomingMovies() + "\n\n" +
                "Khuyến mãi đặc biệt: " + chatbotPreferences.getSpecialPromotions() + "\n\n" +
                "Hãy trả lời các câu hỏi của khách hàng một cách thân thiện, ngắn gọn và đầy đủ thông tin. " +
                "Luôn sử dụng tiếng Việt trong câu trả lời. Nếu bạn không biết câu trả lời, hãy mời khách hàng liên hệ trực tiếp với rạp chiếu phim.";
    }


}