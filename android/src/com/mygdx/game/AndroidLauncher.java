package com.mygdx.game;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

import java.io.IOException;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import static android.os.Environment.getExternalStorageDirectory;


public class AndroidLauncher extends AndroidApplication {




	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGLSurfaceView20API18 = true;

	//	Toast.makeText(this, getExternalStorageDirectory().toURI().toString(),Toast.LENGTH_SHORT).show();




		initialize(new MyGdxGame(), config);


		try {
			for (  String d :  getAssets().list("")){

				System.out.println(d);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	private boolean checkGL20Support( Context context )
	{
		EGL10 egl = (EGL10) EGLContext.getEGL();
		EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		int[] version = new int[2];
		egl.eglInitialize(display, version);

		int EGL_OPENGL_ES2_BIT = 4;
		int[] configAttribs =
				{
						EGL10.EGL_RED_SIZE, 4,
						EGL10.EGL_GREEN_SIZE, 4,
						EGL10.EGL_BLUE_SIZE, 4,
						EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
						EGL10.EGL_NONE
				};

		EGLConfig[] configs = new EGLConfig[10];
		int[] num_config = new int[1];
		egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);
		egl.eglTerminate(display);
		return num_config[0] > 0;
	}
}
