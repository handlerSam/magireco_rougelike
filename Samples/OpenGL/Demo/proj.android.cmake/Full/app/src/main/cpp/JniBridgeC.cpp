/**
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

#include <jni.h>
#include "JniBridgeC.hpp"
#include "LAppDelegate.hpp"
#include "LAppPal.hpp"
#include "LAppLive2DManager.hpp"
#include "LAppView.hpp"

using namespace Csm;

static JavaVM* g_JVM; // JavaVM is valid for all threads, so just save it globally
static jclass  g_JniBridgeJavaClass;
static jmethodID g_LoadFileMethodId;
static jmethodID g_MoveTaskToBackMethodId;

JNIEnv* GetEnv()
{
    JNIEnv* env = NULL;
    g_JVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
    return env;
}

// The VM calls JNI_OnLoad when the native library is loaded
jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
    g_JVM = vm;

    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK)
    {
        return JNI_ERR;
    }

    jclass clazz = env->FindClass("com/live2d/rougelike/JniBridgeJava");
    g_JniBridgeJavaClass = reinterpret_cast<jclass>(env->NewGlobalRef(clazz));
    g_LoadFileMethodId = env->GetStaticMethodID(g_JniBridgeJavaClass, "LoadFile", "(Ljava/lang/String;)[B");
    g_MoveTaskToBackMethodId = env->GetStaticMethodID(g_JniBridgeJavaClass, "MoveTaskToBack", "()V");

    return JNI_VERSION_1_6;
}

void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
{
    JNIEnv *env = GetEnv();
    env->DeleteGlobalRef(g_JniBridgeJavaClass);
}

char* JniBridgeC::LoadFileAsBytesFromJava(const char* filePath, unsigned int* outSize)
{
    JNIEnv *env = GetEnv();

    // ファイルロード
    jbyteArray obj = (jbyteArray)env->CallStaticObjectMethod(g_JniBridgeJavaClass, g_LoadFileMethodId, env->NewStringUTF(filePath));
    *outSize = static_cast<unsigned int>(env->GetArrayLength(obj));

    char* buffer = new char[*outSize];
    env->GetByteArrayRegion(obj, 0, *outSize, reinterpret_cast<jbyte *>(buffer));

    return buffer;
}

void JniBridgeC::MoveTaskToBack()
{
    JNIEnv *env = GetEnv();

    // アプリ終了
    env->CallStaticVoidMethod(g_JniBridgeJavaClass, g_MoveTaskToBackMethodId, NULL);
}

std::string jstring2str(JNIEnv* env, jstring jstr)
{
    char* rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");
    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid  = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
    jsize alen = env->GetArrayLength(barr);
    if(alen == 0){
        return std::string("");
    }
    jbyte* ba = env->GetByteArrayElements(barr,JNI_FALSE);
    if(alen > 0)
    {
        rtn = (char*)malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);
    std::string stemp(rtn);
    free(rtn);
    return stemp;
}

extern "C"
{
    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnStart(JNIEnv *env, jclass type)
    {
        LAppDelegate::GetInstance()->OnStart();
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnPause(JNIEnv *env, jclass type)
    {
        LAppDelegate::GetInstance()->OnPause();
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnStop(JNIEnv *env, jclass type)
    {
        LAppDelegate::GetInstance()->OnStop();
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnDestroy(JNIEnv *env, jclass type)
    {
        LAppDelegate::GetInstance()->OnDestroy();
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnSurfaceCreated(JNIEnv *env, jclass type)
    {
        LAppDelegate::GetInstance()->OnSurfaceCreate();
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnSurfaceChanged(JNIEnv *env, jclass type, jint width, jint height)
    {
        LAppDelegate::GetInstance()->OnSurfaceChanged(width, height);
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnDrawFrame(JNIEnv *env, jclass type)
    {
        LAppDelegate::GetInstance()->Run();
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnTouchesBegan(JNIEnv *env, jclass type, jfloat pointX, jfloat pointY)
    {
        LAppDelegate::GetInstance()->OnTouchBegan(pointX, pointY);
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnTouchesEnded(JNIEnv *env, jclass type, jfloat pointX, jfloat pointY)
    {
        LAppDelegate::GetInstance()->OnTouchEnded(pointX, pointY);
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeOnTouchesMoved(JNIEnv *env, jclass type, jfloat pointX, jfloat pointY)
    {
        LAppDelegate::GetInstance()->OnTouchMoved(pointX, pointY);
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeSetCharacter(JNIEnv *env, jclass type, jstring char0id, jstring char0motion, jstring char0face, jstring char1id, jstring char1motion, jstring char1face, jstring char2id, jstring char2motion, jstring char2face)
    {
        LAppLive2DManager *Live2DManager = LAppLive2DManager::GetInstance();
        Live2DManager->setCharacter(jstring2str(env, char0id),jstring2str(env, char0motion),jstring2str(env, char0face),jstring2str(env, char1id),jstring2str(env, char1motion),jstring2str(env, char1face),jstring2str(env, char2id),jstring2str(env, char2motion),jstring2str(env, char2face));

    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeSetAlpha(JNIEnv *env, jclass type, jfloat alpha)
    {
        LAppView::model_alpha = alpha;
    }

    JNIEXPORT void JNICALL
    Java_com_live2d_rougelike_JniBridgeJava_nativeChangeBackground(JNIEnv *env, jclass type, jstring index)
    {
        LAppDelegate::GetInstance()->ChangePassBackgroundName(jstring2str(env, index));
    }

JNIEXPORT void JNICALL
Java_com_live2d_rougelike_JniBridgeJava_setScreenSize(JNIEnv *env, jclass type, jint width, jint height)
{
    LAppDelegate::GetInstance()->ChangeScreenWidth(width,height);
}
}

