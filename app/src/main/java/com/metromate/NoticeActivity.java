package com.metromate;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice); // XML 파일 연결

        // 공지사항 데이터 설정
        TextView tvDate = findViewById(R.id.tv_notice_date);
        TextView tvTitle = findViewById(R.id.tv_notice_title);
        TextView tvContent = findViewById(R.id.tv_notice_content);

        // 동적으로 내용 설정
        tvDate.setText("게시 날짜: 2024.12.08");
        tvTitle.setText("앱 업데이트 안내");
        tvContent.setText("안녕하세요. 이번 업데이트에서는 새로운 기능이 추가되었습니다.\n\n자세한 내용은 공지사항을 확인해주세요.\n\n감사합니다.");
    }
}
