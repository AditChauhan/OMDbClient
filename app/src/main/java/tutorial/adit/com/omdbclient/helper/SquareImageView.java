/*
 * Copyright (c) 2019. Created By Adit Chauhan
 */

package tutorial.adit.com.omdbclient.helper;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SquareImageView extends AppCompatImageView
{
	public SquareImageView(Context context)
	{
		super(context);
	}

	public SquareImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public SquareImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
	}
}
