package com.helloarron.dhroid.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.helloarron.dhroid.adapter.ValueFix;
import com.helloarron.dhroid.ioc.IocContainer;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewUtil {

    /**
     * 切换视图的显示
     *
     * @param v 切换后可见为true else false
     */
    public static boolean toggle(View v) {
        if (v.getVisibility() == View.GONE) {
            v.setVisibility(View.VISIBLE);
            return true;
        } else {
            v.setVisibility(View.GONE);
            return false;
        }
    }

    public interface ToggleCount {
        /**
         * @param count 由 1 开始
         * @return false 执行第一次
         */
        public boolean atCount(Integer count);

    }

    /**
     * source 切换切换视图的显示
     *
     * @param source
     * @param target
     */
    public static void setToggle(View source, final View target) {
        source.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                toggle(target);
            }
        });
    }

    public interface RoundToggle {
        /**
         * @param source
         * @param target
         * @param visiable target的可见型
         */
        public void before(View source, final View target, boolean visiable);

        public void after(View source, final View target, boolean visiable);
    }

    public static void setToggle(final View source, final View target, final RoundToggle round) {
        source.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (target.getVisibility() == View.VISIBLE) {
                    round.before(source, target, true);
                } else {
                    round.before(source, target, false);
                }
                round.after(source, target, toggle(target));
            }
        });
    }

    public static void setToggleAndPerform(View source, final View target) {
        source.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                toggle(target);
            }
        });
        source.performClick();
    }

    public static void setToggleAndPerform(final View source, final View target, final RoundToggle round) {
        source.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (target.getVisibility() == View.VISIBLE) {
                    round.before(source, target, true);
                } else {
                    round.before(source, target, false);
                }
                round.after(source, target, toggle(target));
            }
        });
        source.performClick();
    }

    /**
     * 对象绑定视图
     *
     * @param v
     * @param value
     */
    public static void bindView(View v, Object value) {
        if (v == null || value == null)
            return;
        if (v instanceof TextView) {
            if (value instanceof CharSequence) {
                ((TextView) v).setText((CharSequence) value);
            } else {
                ((TextView) v).setText(value.toString());
            }
        }
        if (v instanceof ImageView) {
            if (value instanceof String) {
                ImageLoader.getInstance().displayImage(value.toString(), (ImageView) v);
            } else if (value instanceof Drawable) {
                ((ImageView) v).setImageDrawable((Drawable) value);
            } else if (value instanceof Bitmap) {
                ((ImageView) v).setImageBitmap((Bitmap) value);
            } else if (value instanceof Integer) {
                ((ImageView) v).setImageResource((Integer) value);
            }
        }
    }

    /**
     * 对象绑定视图,无结果显示默认值
     *
     * @param v
     * @param value
     */
    public static void bindViewOrDefault(View v, Object value, String defaultValue) {
        if (TextUtils.isEmpty(value.toString())) {
            if (v instanceof TextView) {
                ((TextView) v).setText(defaultValue);
            }
            return;
        }
        if (v instanceof TextView) {
            if (value instanceof CharSequence) {
                ((TextView) v).setText((CharSequence) value);
            } else {
                ((TextView) v).setText(value.toString());
            }
        }
        if (v instanceof ImageView) {
            if (value instanceof String) {
                ImageLoader.getInstance().displayImage(value.toString(), (ImageView) v);
            } else if (value instanceof Drawable) {
                ((ImageView) v).setImageDrawable((Drawable) value);
            } else if (value instanceof Bitmap) {
                ((ImageView) v).setImageBitmap((Bitmap) value);
            } else if (value instanceof Integer) {
                ((ImageView) v).setImageResource((Integer) value);
            }
        }
    }

    public static void bindNetImage(ImageView v, String url, String type) {
        ValueFix fix = IocContainer.getShare().get(ValueFix.class);
        if (fix != null) {
            ImageLoader.getInstance().displayImage(url, (ImageView) v, fix.imageOptions(type));
        } else {
            ImageLoader.getInstance().displayImage(url, (ImageView) v);
        }
    }

    /**
     * 对象绑定视图,同时通过全局的数据修复
     *
     * @param v
     * @param value
     */
    public static void bindView(View v, Object value, String type) {
        ValueFix fix = IocContainer.getShare().get(ValueFix.class);
        if (fix != null) {
            value = fix.fix(value, type);
        }
        bindView(v, value);
    }

    public static boolean isEmpty(TextView... texts) {
        if (texts == null)
            return false;
        for (int i = 0; i < texts.length; i++) {
            boolean empty = TextUtils.isEmpty(texts[i].getText().toString().trim());
            if (empty) {
                return empty;
            }
        }
        return false;
    }

}
