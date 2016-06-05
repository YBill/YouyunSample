package cn.youyunsample.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.youyunsample.R;
import cn.youyunsample.base.BaseActivity;
import cn.youyunsample.chat.adapter.ExpressionGridViewAdapter;
import cn.youyunsample.chat.adapter.ExpressionPagerAdapter;
import cn.youyunsample.widgets.ExpandGridView;

/**
 * Created by 卫彪 on 2016/6/3.
 */
public class ChatActivity extends BaseActivity<ChatView, ChatPresenter> implements ChatView, View.OnClickListener{

    private Button buttonSetModeVoice; // 左侧语音键盘按钮
    private View buttonPressToSpeak; // 按住说话按钮
    private RelativeLayout editLayout; // 输入框外层View,包括EditText和emoji表情按钮
    private EditText mEditTextContent; // 输入框
    private ImageView emojiIcon; // emoji图标
    private Button btnMore; // 右侧更多按钮(加号)
    private View buttonSend; // 发送按钮,跟当btnMore隐藏时显示
    private View modeView; // 底部更多View,包括表情或拍照等按钮
    private ViewPager expressionViewPager; // 表情ViewPager
    private View emojiIconContainer; // 底部表情View
    private View btnContainer; // 底部扩展按钮,拍照和相册等
    private TextView photoGalleryBtn; // 相册
    private TextView takePhotoBtn; // 拍照
    private ListView chatListView; // 聊天界面

    /**
     * true: 显示键盘图标，按住说话按钮
     * false: 显示语音图标，输入框
     */
    private boolean isSpeck = false;
    private InputMethodManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initViews();
        initListener();
    }

    @Override
    protected ChatPresenter initPresenter() {
        return new ChatPresenter();
    }

    private void initViews() {
        buttonSetModeVoice = (Button) findViewById(R.id.btn_set_mode_voice);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        editLayout = (RelativeLayout) findViewById(R.id.edittext_layout);
        editLayout.setBackgroundResource(R.drawable.input_bar_bg_normal);
        editLayout.requestFocus();
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
        emojiIcon = (ImageView) findViewById(R.id.iv_emoticons_normal);
        btnMore = (Button) findViewById(R.id.btn_more);
        buttonSend = findViewById(R.id.btn_send);
        modeView = findViewById(R.id.ll_more);
        expressionViewPager = (ViewPager) findViewById(R.id.vp_emoji);
        emojiIconContainer = findViewById(R.id.ll_face_container);
        btnContainer = findViewById(R.id.ll_btn_container);
        photoGalleryBtn = (TextView) findViewById(R.id.tv_picture);
        takePhotoBtn = (TextView) findViewById(R.id.tv_take_photo);
        chatListView = (ListView) findViewById(R.id.lv_chat);
    }

    private void initListener() {
        buttonSetModeVoice.setOnClickListener(this);
        mEditTextContent.setOnClickListener(this);
        emojiIcon.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        chatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                modeView.setVisibility(View.GONE);
                return false;
            }
        });
        mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editLayout.setBackgroundResource(R.drawable.input_bar_bg_active);
                } else {
                    editLayout.setBackgroundResource(R.drawable.input_bar_bg_normal);
                }

            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private String getEmojiStringByUnicode(int unicodeJoy) {
        return new String(Character.toChars(unicodeJoy));
    }

    private  List<View> emojiLists;
    private void showEmojiView(){
        if(emojiLists == null){
            emojiLists = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                emojiLists.add(getEmojiItemViews(i));
            }
            expressionViewPager.setAdapter(new ExpressionPagerAdapter(emojiLists));
        }
    }

    /**
     * Emoji Item 系统共79个常用表情
     * @param page
     * @return
     */
    private View getEmojiItemViews(int page){
        List<String> list = new ArrayList<>();
        int start = 21 * page;
        int length;
        if(page == 3)
            length = 79;
        else
            length = (page+1)*21;
        for (int i = start; i < length; i++){
            list.add(getEmojiStringByUnicode(0x1F601 + i));
        }
        View view  = View.inflate(ChatActivity.this, R.layout.layout_expression_gridview, null);
        ExpandGridView gridView = (ExpandGridView) view.findViewById(R.id.gridview);
        final ExpressionGridViewAdapter gridViewAdapter = new ExpressionGridViewAdapter(ChatActivity.this, list);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEditTextContent.append(gridViewAdapter.getItem(position));
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set_mode_voice:
                isSpeck = !isSpeck;
                if(isSpeck){
                    hideKeyboard();
                    buttonSetModeVoice.setBackgroundResource(R.drawable.chatting_setmode_keyboard_btn_normal);
                    editLayout.setVisibility(View.GONE);
                    buttonPressToSpeak.setVisibility(View.VISIBLE);
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }else{
                    buttonSetModeVoice.setBackgroundResource(R.drawable.icon_chat_voice);
                    buttonPressToSpeak.setVisibility(View.GONE);
                    editLayout.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(mEditTextContent.getText().toString())) {
                        btnMore.setVisibility(View.VISIBLE);
                        buttonSend.setVisibility(View.GONE);
                    } else {
                        btnMore.setVisibility(View.GONE);
                        buttonSend.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.iv_emoticons_normal:
                hideKeyboard();
                showEmojiView();
                modeView.setVisibility(View.VISIBLE);
                emojiIconContainer.setVisibility(View.VISIBLE);
                btnContainer.setVisibility(View.GONE);
                break;
            case R.id.btn_more:
                hideKeyboard();
                modeView.setVisibility(View.VISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.et_sendmessage:
                editLayout.setBackgroundResource(R.drawable.input_bar_bg_active);
                modeView.setVisibility(View.GONE);
                break;
        }
    }

    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void showLoadView() {

    }

    @Override
    public void hideLoadView() {

    }
}
