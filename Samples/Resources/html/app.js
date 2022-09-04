var active;

var Handler_characterId;

var Handler_spriteId;

var Handler_characterName;

var Handler_spriteName;

var Handler_canvasWidth = 1200;

var Handler_prefix;

var Handler_context;

var Handler_armature;

var Handler_scale;

var Handler_offset;

var Handler_animation_index = 0;
//用于标识此次动画是做什么的
//0不做任何事 1我方攻击进入动画 2我方攻击结束动画 3敌方攻击进入动画 4敌方攻击结束动画 5动画结束后自动切换为wait
//6动画结束后自动切换为stance

var SpriteLayer = cc.Layer.extend({
    sprite:null,
    ctor:function (resource) {
        this._super();

        active = this;
        var size = cc.winSize;
        console.log("winSize:",size.width,size.height);
        var bgLayer = new cc.LayerColor(bg_color);
        this.addChild(bgLayer, 0);

        ccs.armatureDataManager.addArmatureFileInfo(resource);

        var name = resource.replace(base_url, "").replace(".ExportJson","");
        var scale = Handler_scale;
        console.log("scale:",Handler_scale);
        var offset = Handler_offset;

        if (name.slice(-4) == "_m_r" || name.slice(-4) == "_d_r"){
            scale = 0.5;
            offset = 0;
        }

        Handler_armature = new ccs.Armature(name);

        for (var i in Handler_armature.getAnimation()._animationData.movementNames){
            if (Handler_armature.getAnimation()._animationData.movementNames[i] == Handler_spriteName){
                Handler_spriteId = i;
                console.log("found_movement:",i);
                break;
            }
            console.log("movement:",Handler_armature.getAnimation()._animationData.movementNames[i]);
        }

        
        Handler_armature.getAnimation().playWithIndex(Handler_spriteId);// 设置采用几号动画，名字在armature.getAnimation()._animationData.movementNames[i]里
        
        Handler_armature.getAnimation().setMovementEventCallFunc(function (armature, movementType, movementID){
            if(movementType == ccs.MovementEventType.complete){
                console.log("complete");
                if(Handler_animation_index == 5){
                    Handler_spriteName = "wait";
                    setTimeout("changeSprite()",500);
                    Handler_animation_index = 0;
                }else if(Handler_animation_index == 6){
                    Handler_spriteName = "stance";
                    setTimeout("changeSprite()",500);
                    Handler_animation_index = 0;
                }else if(Handler_animation_index !== 0){
                    AndroidMethod.animationEndCallback(Handler_animation_index);
                    Handler_animation_index = 0;
                }
            }else if(movementType == ccs.MovementEventType.loopComplete){
                console.log("loopcomplete");
            }
            // else if(movementType == ccs.MovementEventType.start){
            //     console.log("start");
            // }
        });
        
        Handler_armature.scale = scale;
        if(scale == 1){
            Handler_armature.x = size.width / 2;
            Handler_armature.y = (size.height / 2) - offset;
        }else{
            Handler_armature.x = 50;
            Handler_armature.y = size.height - offset;
        }
        this.addChild(Handler_armature);


        // var stringAnimations = "";
        // for (var i in armature.getAnimation()._animationData.movementNames){
        //     if (armature.getAnimation()._animationData.movementNames[i] == "wait" ||
        //         armature.getAnimation()._animationData.movementNames[i] == "action_in")
        //         stringAnimations += "<option value=\"" + i + "\" selected>" + armature.getAnimation()._animationData.movementNames[i] + "</option>";
        //     else
        //         stringAnimations += "<option value=\"" + i + "\">" + armature.getAnimation()._animationData.movementNames[i] + "</option>";
        // }
        // $("#select_animation").html(stringAnimations);
        // $("#select_animation").off("change").on("change", function(){
        //     armature.getAnimation().playWithIndex($("#select_animation").val());
        // });

        return true;
    },
    onEnter: function(){
        // $("#select_animation").trigger("change");

    }
});

var SpriteScene = cc.Scene.extend({
    onEnter:function () {
        this._super();
        var layer = new SpriteLayer(window.resources[0]);
        this.addChild(layer);
    }
});

var BackgroundLayer = cc.Layer.extend({
    sprite: null,
    ctor: function(){
        this._super();
        active = this;
        var bgLayer = new cc.LayerColor(bg_color);
        this.addChild(bgLayer, 0);
    }
});

var BackgroundScene = cc.Scene.extend({
    onEnter: function()  {
        this._super();
        var layer = new BackgroundLayer();
        this.addChild(layer);
    }
});

var app = {};

app.ChangeBackground = function(){
    active.children[0].color = bg_color;
}

