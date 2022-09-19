/**
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

#include "LAppLive2DManager.hpp"
#include <string>
#include <GLES2/gl2.h>
#include <Rendering/CubismRenderer.hpp>
#include "LAppPal.hpp"
#include "LAppDefine.hpp"
#include "LAppDelegate.hpp"
#include "LAppModel.hpp"
#include "LAppView.hpp"


//#define USE_MODEL_RENDER_TARGET
#define USE_RENDER_TARGET

std::string modelId0 = "";
std::string passFace0 = "";
std::string passMotion0 = "";
int position0 = -1;

std::string modelId1 = "";
std::string passFace1 = "";
std::string passMotion1 = "";
int position1 = -1;

std::string modelId2 = "";
std::string passFace2 = "";
std::string passMotion2 = "";
int position2 = -1;

bool modelChangeFlag = false;

using namespace Csm;
using namespace LAppDefine;
using namespace std;

namespace {
    LAppLive2DManager* s_instance = NULL;

    void FinishedMotion(ACubismMotion* self)
    {
        LAppPal::PrintLog("Motion Finished: %x", self);
    }
}

LAppLive2DManager* LAppLive2DManager::GetInstance()
{
    if (s_instance == NULL)
    {
        s_instance = new LAppLive2DManager();
    }

    return s_instance;
}

void LAppLive2DManager::ReleaseInstance()
{
    if (s_instance != NULL)
    {
        delete s_instance;
    }

    s_instance = NULL;
}

LAppLive2DManager::LAppLive2DManager()
    : _viewMatrix(NULL)
    , _sceneIndex(0)
{
    _viewMatrix = new CubismMatrix44();

    ChangeScene(_sceneIndex);
}

LAppLive2DManager::~LAppLive2DManager()
{
    ReleaseAllModel();
}

void LAppLive2DManager::ReleaseAllModel()
{
    for (csmUint32 i = 0; i < _models.GetSize(); i++)
    {
        delete _models[i];
    }

    _models.Clear();
}

LAppModel* LAppLive2DManager::GetModel(csmUint32 no) const
{
    if (no < _models.GetSize())
    {
        return _models[no];
    }

    return NULL;
}

void LAppLive2DManager::OnDrag(csmFloat32 x, csmFloat32 y) const
{
    for (csmUint32 i = 0; i < _models.GetSize(); i++)
    {
        LAppModel* model = GetModel(i);

        model->SetDragging(x, y);
    }
}

void LAppLive2DManager::OnTap(csmFloat32 x, csmFloat32 y)
{
    if (DebugLogEnable)
    {
        LAppPal::PrintLog("[APP]tap point: {x:%.2f y:%.2f}", x, y);
    }

    for (csmUint32 i = 0; i < _models.GetSize(); i++)
    {
        if (_models[i]->HitTest(HitAreaNameHead, x, y))
        {
            if (DebugLogEnable)
            {
                LAppPal::PrintLog("[APP]hit area: [%s]", HitAreaNameHead);
            }
            _models[i]->SetRandomExpression();
        }
        else if (_models[i]->HitTest(HitAreaNameBody, x, y))
        {
            if (DebugLogEnable)
            {
                LAppPal::PrintLog("[APP]hit area: [%s]", HitAreaNameBody);
            }
            _models[i]->StartRandomMotion(MotionGroupTapBody, PriorityNormal, FinishedMotion);
        }
    }
}

void LAppLive2DManager::OnUpdate()
{
    int width = LAppDelegate::GetInstance()->GetWindowWidth();
    int height = LAppDelegate::GetInstance()->GetWindowHeight();

    csmUint32 modelCount = _models.GetSize();
    for (csmUint32 i = 0; i < modelCount; ++i)
    {
        CubismMatrix44 projection;
        LAppModel* model = GetModel(i);
        if (model->GetModel()->GetCanvasWidth() > 1.0f && width < height)
        {
            // 横に長いモデルを縦長ウィンドウに表示する際モデルの横サイズでscaleを算出する
            model->GetModelMatrix()->SetWidth(2.0f);
            projection.Scale(1.0f, static_cast<float>(width) / static_cast<float>(height));
        }
        else
        {
            projection.Scale(static_cast<float>(height) / static_cast<float>(width), 1.0f);
        }
        // 必要があればここで乗算
        if (_viewMatrix != NULL)
        {
            projection.MultiplyByMatrix(_viewMatrix);
        }
        // モデル1体描画前コール
        LAppDelegate::GetInstance()->GetView()->PreModelDraw(*model);
        model->Update();
        model->Draw(projection);///< 参照渡しなのでprojectionは変質する
        // モデル1体描画後コール
        LAppDelegate::GetInstance()->GetView()->PostModelDraw(*model);
    }

    if(modelChangeFlag){
        ChangeScene(0);
        modelChangeFlag = false;
    }

    if (passFace0 != "" && position0 != -1){
        _models[position0]->SetExpression(("mtn_ex_"+passFace0+".exp3.json").c_str());// 格式: mtn_ex_050.exp3.json
        passFace0 = "";
    }
    if (passFace1 != "" && position1 != -1){
        _models[position1]->SetExpression(("mtn_ex_"+passFace1+".exp3.json").c_str());
        passFace1 = "";
    }
    if (passFace2 != "" && position2 != -1){
        _models[position2]->SetExpression(("mtn_ex_"+passFace2+".exp3.json").c_str());
        passFace2 = "";
    }

    if (passMotion0 != "" && position0 != -1){
        //LAppPal::PrintLog(("[SGY]Model0 motion change! " + passMotion0).c_str());
        _models[position0]->StartMotion(MotionGroupTapBody, -1, PriorityForce, FinishedMotion, ("mtn/motion_"+passMotion0+".motion3.json").c_str()); // 格式: mtn/motion_000.motion3.json
        passMotion0 = "";
    }
    if (passMotion1 != "" && position1 != -1){
        _models[position1]->StartMotion(MotionGroupTapBody, -1, PriorityForce, FinishedMotion, ("mtn/motion_"+passMotion1+".motion3.json").c_str()); // 格式: mtn/motion_000.motion3.json
        passMotion1 = "";
    }
    if (passMotion2 != "" && position2 != -1){
        _models[position2]->StartMotion(MotionGroupTapBody, -1, PriorityForce, FinishedMotion, ("mtn/motion_"+passMotion2+".motion3.json").c_str()); // 格式: mtn/motion_000.motion3.json
        passMotion2 = "";
    }


}

void LAppLive2DManager::NextScene()
{
    csmInt32 no = (_sceneIndex + 1) % ModelDirSize;
    ChangeScene(no);
}

void LAppLive2DManager::ChangeScene(Csm::csmInt32 index)
{
    _sceneIndex = index;
    if (DebugLogEnable)
    {
        LAppPal::PrintLog("[APP]model index: %d", _sceneIndex);
    }

    // ModelDir[]に保持したディレクトリ名から
    // model3.jsonのパスを決定する.
    // ディレクトリ名とmodel3.jsonの名前を一致させておくこと.
    //std::string model = ModelDir[index];
    std::string modelPath; //= ResourcesPath + model + "/";
    std::string modelJsonName = "model.model3.json";

//    ReleaseAllModel();
//    _models.PushBack(new LAppModel());
//    _models.PushBack(new LAppModel());
//
//    _models[0]->LoadAssets(modelPath.c_str(), modelJsonName.c_str());
//    _models[0]->GetModelMatrix()->TranslateX(-1.0f);
//
//    _models[1]->LoadAssets(modelPath.c_str(), modelJsonName.c_str());
//    _models[1]->GetModelMatrix()->TranslateX(1.0f);

    ReleaseAllModel();
    LAppPal::PrintLog(("[SGY]Model Change! " + modelId0 + "/" + modelId1 + "/" + modelId2).c_str());
    int temp_position = 0;
    if(!modelId0.empty()){
        _models.PushBack(new LAppModel());
        modelPath = ResourcesPath + modelId0 + "/";
        position0 = temp_position;
        temp_position++;
        _models[position0]->LoadAssets(modelPath.c_str(), modelJsonName.c_str());
        _models[position0]->GetModelMatrix()->TranslateX(-1.0f);
        LAppPal::PrintLog("[SGY]Model0 Create!");
    }else{
        position0 = -1;
    }
    if(!modelId1.empty()){
        _models.PushBack(new LAppModel());
        modelPath = ResourcesPath + modelId1 + "/";
        position1 = temp_position;
        temp_position++;
        _models[position1]->LoadAssets(modelPath.c_str(), modelJsonName.c_str());
        LAppPal::PrintLog("[SGY]Model1 Create!");
    }else{
        position1 = -1;
    }
    if(!modelId2.empty()){
        _models.PushBack(new LAppModel());
        modelPath = ResourcesPath + modelId2 + "/";
        position2 = temp_position;
        _models[position2]->LoadAssets(modelPath.c_str(), modelJsonName.c_str());
        _models[position2]->GetModelMatrix()->TranslateX(1.0f);
        LAppPal::PrintLog("[SGY]Model2 Create!");
    }else{
        position2 = -1;
    }


    /*
     * モデル半透明表示を行うサンプルを提示する。
     * ここでUSE_RENDER_TARGET、USE_MODEL_RENDER_TARGETが定義されている場合
     * 別のレンダリングターゲットにモデルを描画し、描画結果をテクスチャとして別のスプライトに張り付ける。
     */
    {
#if defined(USE_RENDER_TARGET)
        // LAppViewの持つターゲットに描画を行う場合、こちらを選択
        LAppView::SelectTarget useRenderTarget = LAppView::SelectTarget_ViewFrameBuffer;
#elif defined(USE_MODEL_RENDER_TARGET)
        // 各LAppModelの持つターゲットに描画を行う場合、こちらを選択
        LAppView::SelectTarget useRenderTarget = LAppView::SelectTarget_ModelFrameBuffer;
#else
        // デフォルトのメインフレームバッファへレンダリングする(通常)
        LAppView::SelectTarget useRenderTarget = LAppView::SelectTarget_None;
#endif

#if defined(USE_RENDER_TARGET) || defined(USE_MODEL_RENDER_TARGET)
        // モデル個別にαを付けるサンプルとして、もう1体モデルを作成し、少し位置をずらす
        //_models.PushBack(new LAppModel());
        //_models[1]->LoadAssets(modelPath.c_str(), modelJsonName.c_str());
        //_models[1]->GetModelMatrix()->TranslateX(0.2f);
#endif

        LAppDelegate::GetInstance()->GetView()->SwitchRenderingTarget(useRenderTarget);

        // 別レンダリング先を選択した際の背景クリア色
        float clearColor[3] = { 1.0f, 1.0f, 1.0f };
        LAppDelegate::GetInstance()->GetView()->SetRenderTargetClearColor(clearColor[0], clearColor[1], clearColor[2]);
    }
}

csmUint32 LAppLive2DManager::GetModelNum() const
{
    return _models.GetSize();
}

void LAppLive2DManager::SetViewMatrix(CubismMatrix44* m)
{
    for (int i = 0; i < 16; i++) {
        _viewMatrix->GetArray()[i] = m->GetArray()[i];
    }
}

void LAppLive2DManager::setCharacter(std::string char0id, std::string char0motion, std::string char0face, std::string char1id, std::string char1motion, std::string char1face, std::string char2id, std::string char2motion, std::string char2face){
    LAppPal::PrintLog(("[SGY]Model Change will Receive! " + modelId0 + "/" + modelId1 + "/" + modelId2).c_str());
    if(modelId0 != char0id){
        modelId0 = char0id;
        modelChangeFlag = true;
    }
    if(modelId1 != char1id){
        modelId1 = char1id;
        modelChangeFlag = true;
    }
    if(modelId2 != char2id){
        modelId2 = char2id;
        modelChangeFlag = true;
    }
    LAppPal::PrintLog(("[SGY]Model Change Received! " + modelId0 + "/" + modelId1 + "/" + modelId2).c_str());

    passMotion0 = char0motion;
    passFace0 = char0face;
    passMotion1 = char1motion;
    passFace1 = char1face;
    passMotion2 = char2motion;
    passFace2 = char2face;
}
