/**
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.rougelike;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Log.d("SGY","onSurfaceCreated_Start"+id);
        JniBridgeJava.nativeOnSurfaceCreated();
        //Log.d("SGY","onSurfaceCreated_End"+id);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Log.d("SGY","onSurfaceChanged_Start"+id);
        JniBridgeJava.nativeOnSurfaceChanged(width, height);
        //Log.d("SGY","onSurfaceChanged_End"+id);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //Log.d("SGY","onDrawFrame_Start"+id);
        JniBridgeJava.nativeOnDrawFrame();
        //Log.d("SGY","onDrawFrame_End"+id);
    }

}
