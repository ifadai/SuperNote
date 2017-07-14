package app.fadai.supernote.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * Created by miaoyongyong on 2017/2/20.
 */

public class AliPayUtils {

    /**
     * 支付
     *
     */
    public static void openAliPay2Pay(Context context,String qrContent) {
        if (openAlipayPayPage(context,qrContent)) {
            Toast.makeText(context, "跳转中...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean openAlipayPayPage(Context context ,String qrContent) {
        try {
            String qrcode = URLEncoder.encode(qrContent, "utf-8");
            final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + qrcode;
            openUri(context, alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送一个intent
     *
     * @param context
     * @param s
     */
    private static void openUri(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        context.startActivity(intent);
    }
}
