$(document).ready(function(){
    initBaseScene();
});

// function setCharacterIdAndSprite(charId, sprite){
//     Handler_characterId = charId;
//     Handler_spriteId = sprite;
// }

// function setCharacterNameAndSprite(charName, sprite){
//     Handler_characterName = charName;
//     Handler_spriteName = sprite;
// }

function initBaseScene(){
    cc.game.onStart = function(){
        if(!cc.sys.isNative && document.getElementById("cocosLoading")) //If referenced loading.js, please remove it
            document.body.removeChild(document.getElementById("cocosLoading"));

        cc.view.enableRetina(false);
        cc.view.adjustViewPort(true);
        cc.view.setDesignResolutionSize(800, 800);
        cc.director.setDisplayStats(false);
        // cc.director.runScene(new BackgroundScene());

    };
    cc.game.run();
}

function OnChangeCharacter(){
    // var darken = $("<div></div>");
    // var selector = $("<div></div>");
    // var searchContainer = $("<div></div>");
    // var resultContainer = $("<div></div>");
    // var searchField = $("<input>");

    // $(document.body).append(darken);
    // $(document.body).append(selector);
    // selector.append(searchContainer);
    // selector.append(resultContainer);
    // searchContainer.append(searchField);

    // darken.attr("id","darken");
    // darken.addClass("darken");
    // darken.css("top", window.pageYOffset + "px");
    // darken.click(function(){
    //    $('#selector').remove();
    //    $('#darken').remove();
    //    $(document.body).css("overflow", "auto");
    // });

    // selector.attr("id","selector");
    // selector.addClass("selector");
    // selector.css("top", (window.pageYOffset + (window.innerHeight * 0.05)) + "px");

    // searchContainer.attr("id","searchContainer");
    // searchContainer.addClass("searchContainer");
    // searchContainer.css({"padding" : "15px", "text-align" : "center"});

    // resultContainer.attr("id","resultContainer");
    // resultContainer.addClass("resultContainer");

    // searchField.attr("id","searchField");
    // searchField.addClass("form-control");
    // searchField.css({"display" : "inline-block", "width" : "50%"});
    // searchField.on("keyup", function(){
    //    var key = event.keyCode || event.charCode;
    //    SearchResults(key);
    // });

    // LoadResults(CharData);
    //console.log(CharData);
    for(var character in CharData){
        if(CharData[character].NAME.localeCompare(Handler_characterName) == 0){
            Handler_characterId = character;
            console.log("found_character:",character);
            break;
        }
    }

    LoadSpriteSelection(CharData[Handler_characterId]);                
    ChangeSprite(CharData[Handler_characterId]);  
}

function LoadSpriteSelection (data){
    var stringSprites = "";
    for (var key in data.SKIN){
        if (key == "Regular")
            stringSprites += "<option value=\"" + data.SKIN[key] + "\" selected>" + key + "</option>";
        else
            stringSprites += "<option value=\"" + data.SKIN[key] + "\">" + key + "</option>";
    }
    $("#select_sprite").html(stringSprites);
    //$("#select_Sprite").val($("#select_sprite option").filter(function () { return $(this).html() == "Regular"; }).val()).change();
    $("#select_sprite").off("change").on("change", function(){
        ChangeSprite(data);
    });
}

function ChangeSprite(data){
    console.log("onChange");
    cc.game.onStart = function(){
        // console.log("132");
        if(!cc.sys.isNative && document.getElementById("cocosLoading")) //If referenced loading.js, please remove it
            document.body.removeChild(document.getElementById("cocosLoading"));

        // Pass true to enable retina display, disabled by default to improve performance
        cc.view.enableRetina(false);
        // Adjust viewport meta
        cc.view.adjustViewPort(true);
        // Setup the resolution policy and design resolution size
        cc.view.setDesignResolutionSize(Handler_canvasWidth, Handler_canvasWidth);
        // The game will be resized when browser size change
        //cc.view.resizeWithBrowserSize(true);
        //load resources
        ResourceList(data);
        cc.LoaderScene.preload(window.resources, function () {
            cc.director.setClearColor(cc.color(0, 0, 0, 0));
            cc.director.runScene(new SpriteScene());
        }, this);
    };
    cc.game.run();
    console.log("ccGamerun");
}

function ResourceList(data){
    var resource_id = data.SKIN["Regular"];
    // var resource_id = $("#select_sprite").val();
    // console.log(resource_id);
    // console.log(data.SKIN["Regular"]);
    // window.resources = [base_url + "mini_" + resource_id + ".ExportJson",
    //     base_url + "mini_" + resource_id + "0.plist",
    //     base_url + "mini_" + resource_id + "0.png"];
        window.resources = [base_url + Handler_prefix + resource_id + ".ExportJson",
        base_url + Handler_prefix + resource_id + "0.plist",
        base_url + Handler_prefix + resource_id + "0.png"];
}

function OnChangeBackground(){
    // var darken = $("<div></div>");
    // var selector = $("<div></div>");

    // $(document.body).append(darken);
    // $(document.body).append(selector);

    // darken.attr("id","darken");
    // darken.addClass("darken");
    // darken.css("top", window.pageYOffset + "px");
    // darken.click(function(){
    //     $('#selector').remove();
    //     $('#darken').remove();
    //     $(document.body).css("overflow", "auto");
    // });

    // selector.attr("id","selector");
    // selector.addClass("selector");
    // selector.css("top", (window.pageYOffset + (window.innerHeight * 0.05)) + "px");

    // for (var i in BgData){
    //     var img = $('<div></div>');
    //     img.addClass("thumbbutton");
    //     img.attr("id", "thumb_" + i);
    //     img.css({
    //         "background-color": BgData[i].HEX,
    //         "background-size": "120px, 90px"
    //     });
    //     img.on("click", function() {
    //         bg_color = BgData[this.id.slice(-1)].COLOR;
    //         $('#selector').remove();
    //         $('#darken').remove();
    //         $(document.body).css("overflow", "auto");
    //         app.ChangeBackground();
    //     });
    //     selector.append(img);
    // }
}

function OnChangeLog(){
    $(document.body).append($("<div></div>")
        .attr("id","darken")
        .addClass("darken")
        .css("top", window.pageYOffset + "px")
        .click(function(){
            $('#selector').remove();
            $('#darken').remove();
            $(document.body).css("overflow", "auto");
        }))
    .append($("<div></div>")
        .attr("id","selector")
        .addClass("selector")
        .css("top", (window.pageYOffset + (window.innerHeight * 0.05)) + "px")
        .css("padding", "2%"))
    .css("overflow", "hidden");
    $("#selector").append($("<table></table>")
        .addClass("wikitable")
        .append($("<tr></tr>")
            .append($("<td></td>")
                .css("background-color", "#d8d8d8")
                .css("height", "30px")
                .css("padding-left", "8px")
                .attr("align", "left")
                .html("<b>Changelog</b>")
            )
        )
        .append($("<tr></tr>")
            .append($("<td></td>")
                .attr("id", "chglog")
                .css("padding", "15px")
                .css("vertical-align","text-top")
                .attr("align", "left")
            )
        )
    )

    var cb = function (response){
        for (i in response){
            var message = response[i].commit.message;
            var date = response[i].commit.committer.date;
            date = date.replace("T", " ");
            date = date.replace("Z", " UTC");

            $("#chglog").append($("<p></p>")
                .css("line-height", "0.8")
                .html(message+"<br>")
                .append($("<font></font>")
                    .css("font-size", "10px")
                    .css("color", "gray")
                    .html(date)
                )
            );
        }
    }

    // var xobj = new XMLHttpRequest();
    // xobj.open("GET", "https://api.github.com/repos/alg-wiki/magireco-live2d-viewer/commits?sha=gh-pages", true);
    // xobj.onreadystatechange = function () {
    //       if (xobj.readyState == 4 && xobj.status == "200") {
    //         // Required use of an anonymous callback as .open will NOT return a value but simply returns undefined in asynchronous mode
    //         cb(JSON.parse(xobj.response));
    //       }
    // };
    // xobj.send(null); 
}

function showView(){
    console.log("start");
    AndroidMethod.showView();        
}