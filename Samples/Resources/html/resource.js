//var base_url = "assets/sprites/";
var base_url = "";
var bg_color = cc.color(255, 255, 255, 0);
var BgData = [{
	"COLOR": cc.color(0, 255, 0, 255),
	"HEX": "#00FF00"
}, {
	"COLOR": cc.color(0, 0, 255, 255),
	"HEX": "#0000FF"
}, {
	"COLOR": cc.color(255, 0, 0, 255),
	"HEX": "#FF0000"
}, {
	"COLOR": cc.color(255, 255, 255, 255),
	"HEX": "#FFFFFF"
}, {
	"COLOR": cc.color(0, 0, 0, 255),
	"HEX": "#000000"
}];

// START MAGICAL GIRLS 
var CharData = [
	{
		"NAME": "street_day",
		"ID": "9001",
		"ICON": "card_40044_d.png",
		"SKIN": {
			"Regular": "53011_effect",
		}
	},{
		"NAME": "monster_钟摆的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600000_l",
		}
	},{
		"NAME": "monster_立耳的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600100_l",
		}
	},{
		"NAME": "monster_绝交阶梯之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600200_l",
		}
	},{
		"NAME": "monster_待人马之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600300_l",
		}
	},{
		"NAME": "monster_不幸角杯之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600400_l",
		}
	},{
		"NAME": "monster_无名人工智能之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600500_l",
		}
	},{
		"NAME": "monster_记忆馆长之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600600_l",
		}
	},{
		"NAME": "monster_螯合大摩天轮之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600700_l",
		}
	},{
		"NAME": "monster_Flower Speaker之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600800_l",
		}
	},{
		"NAME": "monster_兵熊之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "600900_l",
		}
	},{
		"NAME": "monster_熊后之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "601000_l",
		}
	},{
		"NAME": "monster_沙地的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "610300_l",
		}
	},{
		"NAME": "monster_羊之魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "610400_l",
		}
	},{
		"NAME": "monster_屋顶的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "610500_l",
		}
	},{
		"NAME": "monster_保护孩子的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "610600_l",
		}
	},{
		"NAME": "monster_生神的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "610710_l",
		}
	},{
		"NAME": "monster_班长的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "640000_l",
		}
	},{
		"NAME": "monster_玫瑰园的魔女",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "640100_l",
		}
	},{
		"NAME": "monster_立耳的魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700100_l",
		}
	},{
		"NAME": "monster_绝交挂锁之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700200_l",
		}
	},{
		"NAME": "monster_通灵绘马之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700300_l",
		}
	},{
		"NAME": "monster_不幸猫头鹰之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700400_l",
		}
	},{
		"NAME": "monster_无名邮件之谣(白)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700500_l",
		}
	},{
		"NAME": "monster_无名邮件之谣(黑)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700510_l",
		}
	},{
		"NAME": "monster_记忆职员之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700600_l",
		}
	},{
		"NAME": "monster_螯合吉祥物之谣",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700700_l",
		}
	},{
		"NAME": "monster_工熊之谣(橘)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700900_l",
		}
	},{
		"NAME": "monster_工熊之谣(蓝)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700901_l",
		}
	},{
		"NAME": "monster_工熊之谣(紫)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700902_l",
		}
	},{
		"NAME": "monster_工熊之谣(青)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700903_l",
		}
	},{
		"NAME": "monster_工熊之谣(红)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "700904_l",
		}
	},{
		"NAME": "monster_幸福的魔女的手下(绿)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710122_l",
		}
	},{
		"NAME": "monster_幸福的魔女的手下(黄)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710123_l",
		}
	},{
		"NAME": "monster_幸福的魔女的手下(紫)",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710124_l",
		}
	},{
		"NAME": "monster_镜之魔女的手下Ⅳ",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710200_l",
		}
	},{
		"NAME": "monster_沙地的魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710300_l",
		}
	},{
		"NAME": "monster_羊之魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710400_l",
		}
	},{
		"NAME": "monster_屋顶的魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710500_l",
		}
	},{
		"NAME": "monster_保护孩子的魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710600_l",
		}
	},{
		"NAME": "monster_生神的魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710700_l",
		}
	}, {
		"NAME": "monster_橡胶的魔女的手下",
		"ID": "7407",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710800_l",
		}
	}, {
		"NAME": "monster_流浪的魔女的手下",
		"ID": "7403",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "710900_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅰ",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720100_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅲ(粉)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720300_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅲ(蓝)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720301_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅲ(绿)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720302_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅲ(灰)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720303_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅲ(黑)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720304_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅲ(亮)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720305_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅳ(粉)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720400_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅳ(蓝)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720401_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅳ(绿)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720402_l",
		}
	}, {
		"NAME": "monster_象征的魔女的手下Ⅳ(黑)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720404_l",
		}
	},{
		"NAME": "monster_象征的魔女的手下Ⅳ(黄)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "720405_l",
		}
	},{
		"NAME": "monster_镜之魔女的手下Ⅰ",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "721100_l",
		}
	},{
		"NAME": "monster_镜之魔女的手下Ⅲ",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "721300_l",
		}
	},{
		"NAME": "monster_班长的魔女的手下",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "740000_l",
		}
	},{
		"NAME": "monster_玫瑰园的魔女的手下",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "740100_l",
		}
	},{
		"NAME": "monster_玫瑰园的魔女的手下(圣诞)",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "740110_l",
		}
	},{
		"NAME": "monster_零食的魔女的手下",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "740300_l",
		}
	}, {
		"NAME": "monster3",
		"ID": "7401",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Regular": "740700_l",
		}
	}, {
		"NAME": "Mikuni Oriko Final ver.",
		"ID": "4004",
		"ICON": "card_40044_d.png",
		"SKIN": {
			"Doppel": "400400_d_r",
			"Magia": "400400_m_r",
			"Regular": "400400_r",
			"Regular_09": "400409_r"
		}
	},
	{
		"NAME": "Tamaki Iroha Anime ver.",
		"ID": "1501",
		"ICON": "card_15014_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "150100_d_r",
			"Magia (VIDEO NOT EMBEDDED)": "150100_m_r",
			"Regular": "150100_r",
			"Regular_09": "150109_r"
		}
	},
	{
		"NAME": "Akuma Homura-chan",
		"ID": "2201",
		"ICON": "card_22014_d.png",
		"SKIN": {
			"Doppel": "220100_d_r",
			"Magia": "220100_m_r",
			"Regular": "220100_r",
			"Regular_09": "220109_r"
		}
	},
	{
		"NAME": "Kuroe",
		"ID": "1043",
		"ICON": "card_10434_d.png",
		"SKIN": {
			"Doppel": "104300_d_r",
			"Magia": "104300_m_r",
			"Regular": "104300_r",
			"Regular_09": "104309_r"
		}
	},
	{
		"NAME": "Pernelle Flamel",
		"ID": "4029",
		"ICON": "card_40294_d.png",
		"SKIN": {
			"Doppel": "402900_d_r",
			"Magia": "402900_m_r",
			"Regular": "402900_r",
			"Regular_09": "402909_r"
		}
	},
		{
		"NAME": "Isabeau",
		"ID": "4122",
		"ICON": "card_41224_d.png",
		"SKIN": {
			"Doppel": "412200_d_r",
			"Magia": "412200_m_r",
			"Regular": "412200_r",
			"Regular_09": "412209_r"
		}
	},
		{
		"NAME": "Sakura Kyoko Doppel ver.",
		"ID": "2602",
		"ICON": "card_26024_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "260200_d_r",
			"Magia": "260200_m_r",
			"Regular": "260200_r",
			"Regular_09": "260209_r"
		}
	},
		{
		"NAME": "Miura Asahi",
		"ID": "1034",
		"ICON": "card_10344_d.png",
		"SKIN": {
			"Doppel": "103400_d_r",
			"Magia": "103400_m_r",
			"Regular": "103400_r",
			"Regular_09": "103409_r"
		}
	},
	{
		"NAME": "Nanami Yachiyo Anime ver.",
		"ID": "1302",
		"ICON": "card_13024_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "130200_d_r",
			"Magia (VIDEO NOT EMBEDDED)": "130200_m_r",
			"Regular": "130200_r",
			"Regular_09": "130209_r"
		}
	},
	{
		"NAME": "Tokime Shizuka First Sunrise of the Year ver.",
		"ID": "1125",
		"ICON": "card_11254_d.png",
		"SKIN": {
			"Doppel": "112500_d_r",
			"Magia": "112500_m_r",
			"Regular": "112500_r",
			"Regular_09": "112509_r"
		}
	},
	{
		"NAME": "Nayuta & Mikage Christmas ver.",
		"ID": "1137",
		"ICON": "card_11374_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "113700_d_r",
			"Magia": "113700_m_r",
			"Regular": "113700_r",
			"Regular_09": "113709_r"
		}
	},
	{
		"NAME": "Holy Mami Anime ver.",
		"ID": "2502",
		"ICON": "card_25024_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "250200_d_r",
			"Magia (VIDEO NOT EMBEDDED)": "250200_m_r",
			"Regular": "250200_r",
			"Regular_09": "250209_r"
		}
	},
	{
		"NAME": "Yukari Miyuri",
		"ID": "1032",
		"ICON": "card_10324_d.png",
		"SKIN": {
			"Doppel": "103200_d_r",
			"Magia": "103200_m_r",
			"Magia_50": "103250_m_r",
			"Magia_51": "103251_m_r",
			"Regular": "103200_r",
			"Regular_09": "103209_r",
			"Regular_50": "103250_r",
			"Regular_51": "103251_r"
		}
	},
	{
		"NAME": "Kagura San",
		"ID": "1031",
		"ICON": "card_10314_d.png",
		"SKIN": {
			"Doppel": "103100_d_r",
			"Magia": "103100_m_r",
			"Magia_50": "103150_m_r",
			"Regular": "103100_r",
			"Regular_09": "103109_r",
			"Regular_50": "103150_r"
		}
	},
	{
		"NAME": "Karin & Alina Halloween ver.",
		"ID": "1112",
		"ICON": "card_11124_d.png",
		"SKIN": {
			"Doppel": "111200_d_r",
			"Magia": "111200_m_r",
			"Regular": "111200_r",
			"Regular_09": "111209_r"
		}
	},
	{
		"NAME": "Subaru Kazumi",
		"ID": "4014",
		"ICON": "card_40144_d.png",
		"SKIN": {
			"Doppel": "401400_d_r",
			"Magia": "401400_m_r",
			"Regular": "401400_r",
			"Regular_09": "401409_r"
		}
	},
	{
		"NAME": "Irina Kushu",
		"ID": "3059",
		"ICON": "card_30594_d.png",
		"SKIN": {
			"Doppel": "305900_d_r",
			"Magia": "305900_m_r",
			"Regular": "305900_r",
			"Regular_09": "305909_r"
		}
	},
	{
		"NAME": "Yura Hotaru",
		"ID": "3048",
		"ICON": "card_30483_d.png",
		"SKIN": {
			"Magia": "304800_m_r",
			"Regular": "304800_r",
			"Regular_09": "304809_r"
		}
	},
	{
		"NAME": "Madoka & Iroha",
		"ID": "2104",
		"ICON": "card_21044_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "210400_d_r",
			"Magia": "210400_m_r",
			"Regular": "210400_r",
			"Regular_09": "210409_r"
		}
	},
	{
		"NAME": "Kureha Yuna",
		"ID": "1021",
		"ICON": "card_10214_d.png",
		"SKIN": {
			"Doppel": "102100_d_r",
			"Magia": "102100_m_r",
			"Magia_50": "102150_m_r",
			"Regular": "102100_r",
			"Regular_09": "102109_r",
			"Regular_50": "102150_r",
		}
	},
	{
		"NAME": "Miki Sayaka Surfing ver.",
		"ID": "2401",
		"ICON": "card_24014_d.png",
		"SKIN": {
			"Doppel": "240100_d_r",
			"Magia": "240100_m_r",
			"Regular": "240100_r",
			"Regular_09": "240109_r"
		}
	},
	{
		"NAME": "Momoko & Mitama Mermaid ver.",
		"ID": "1210",
		"ICON": "card_12104_d.png",
		"SKIN": {
			"Doppel (VIDEO NOT EMBEDDED)": "121000_d_r",
			"Magia": "121000_m_r",
			"Regular": "121000_r",
			"Regular_09": "121009_r"
		}
	},
	{
		"NAME": "Nanami Yachiyo Tanabata ver.",
		"ID": "1202",
		"ICON": "card_12024_d.png",
		"SKIN": {
			"Doppel": "120200_d_r",
			"Magia": "120200_m_r",
			"Regular": "120200_r",
			"Regular_09": "120209_r"
		}
	},
	{
		"NAME": "Satomi Nayuta",
		"ID": "1037",
		"ICON": "card_10374_d.png",
		"SKIN": {
			"Doppel": "103700_d_r",
			"Magia": "103700_m_r",
			"Regular": "103700_r",
			"Regular_09": "103709_r"
		}
	},
	{
		"NAME": "Isabeau (Witch)",
		"ID": "4121",
		"ICON": "card_41214_d.png",
		"SKIN": {
		"Doppel": "412100_d_r",
		"Magia": "412100_m_r",
		"Regular": "412100_r",
		"Regular_09": "412109_r"
		}
	}, {
		"NAME": "Kirino Sae",
		"ID": "3055",
		"ICON": "card_30554_d.png",
		"SKIN": {
		   "Doppel": "305500_d_r",
		   "Magia": "305500_m_r",
		   "Regular": "305500_r",
		   "Regular_09": "305509_r"
		}
	}, {
		"NAME": "Konoha & Hazuki",
		"ID": "3503",
		"ICON": "card_35034_d.png",
		"SKIN": {
		"Doppel": "350300_d_r",
		"Magia": "350300_m_r",
		"Regular": "350300_r",
		"Regular_09": "350309_r"
		}
	}, {
		"NAME": "Kurusu Alexandra",
		"ID": "1035",
		"ICON": "card_10354_d.png",
		"SKIN": {
			"Doppel": "103500_d_r",
			"Magia": "103500_m_r",
			"Regular": "103500_r",
			"Regular_09": "103509_r"
		}
	}, {
		"NAME": "Ultimate Madoka-senpai",
		"ID": "2103",
		"ICON": "card_21034_d.png",
		"SKIN": {
			"Doppel": "210300_d_r",
			"Magia": "210300_m_r",
			"Regular": "210300_r",
			"Regular_09": "210309_r",
		}
	}, {
		"NAME": "Miwa Mitsune",
		"ID": "3054",
		"ICON": "card_30544_d.png",
		"SKIN": {
			"Doppel": "305400_d_r",
			"Magia": "305400_m_r",
			"Magia_50": "305450_m_r",
			"Regular": "305400_r",
			"Regular_09": "305409_r",
			"Regular_50": "305450_r",
		}
	}, {
		"NAME": "Rumor Sana",
		"ID": "1104",
		"ICON": "card_11044_d.png",
		"SKIN": {
			"Doppel": "110400_d_r",
			"Magia": "110400_m_r",
			"Regular": "110400_r",
			"Regular_09": "110409_r"
		}
	}, {
		"NAME": "Mikoto Tsubaki",
		"ID": "4036",
		"ICON": "card_40364_d.png",
		"SKIN": {
			"Doppel": "403600_d_r",
			"Magia": "403600_m_r",
			"Magia_50": "403650_m_r",
			"Regular": "403600_r",
			"Regular_09": "403609_r",
			"Regular_50": "403650_r"
		}
	}, {
		"NAME": "Ooba Juri",
		"ID": "1024",
		"ICON": "card_10244_d.png",
		"SKIN": {
			"Doppel": "102400_d_r",
			"Magia": "102400_m_r",
			"Magia_50": "102450_m_r",
			"Regular": "102400_r",
			"Regular_09": "102409_r",
			"Regular_50": "102450_r"
		}
	}, {
		"NAME": "Iroha & Ui Miko ver.",
		"ID": "1401",
		"ICON": "card_14014_d.png",
		"SKIN": {
			"Doppel": "140100_d_r",
			"Magia": "140100_m_r",
			"Regular": "140100_r",
			"Regular_09": "140109_r"
			
		}
	}, { 
		"NAME": "Touka & Nemu Holy Night ver.",
		"ID": "1107",
		"ICON": "card_11074_d.png",
		"SKIN": {
			"Doppel": "110700_d_r",
			"Magia": "110700_m_r",
			"Regular": "110700_r",
			"Regular_09": "110709_r"
		}
	}, {
		"NAME": "Tsuruno & Felicia Delivery ver.",
		"ID": "1203",
		"ICON": "card_12034_d.png",
		"SKIN": {
			"Doppel": "120300_d_r",
			"Magia": "120300_m_r",
			"Regular": "120300_r",
			"Regular_09": "120309_r",
		}
	}, {
		"NAME": "Yakumo Mikage",
		"ID": "1038",
		"ICON": "card_10384_d.png",
		"SKIN": {	
			"Doppel": "103800_d_r",
			"Magia": "103800_m_r",
			"Regular": "103800_r",	
			"Regular_09": "103809_r",
		}
	}, {
		"NAME": "Izumi Kanagi Vampire ver.",
		"ID": "1116",
		"ICON": "card_11164_d.png",
		"SKIN": {	
			"Doppel": "111600_d_r",
			"Magia": "111600_m_r",
			"Regular": "111600_r",	
			"Regular_09": "111609_r",
			"Regular_50": "111650_r",
		}
	}, {
		"NAME": "Togame Momoko Sister ver.",
		"ID": "1110",
		"ICON": "card_11104_d.png",
		"SKIN": {	
			"Doppel": "111000_d_r",
			"Magia": "111000_m_r",
			"Regular": "111000_r",
			"Regular_09": "111009_r",	
		}
	}, {
		"NAME": "Anna Meru",
		"ID": "3037",
		"ICON": "card_30373_d.png",
		"SKIN": {
			"Doppel": "303700_d_r",
			"Magia": "303700_m_r",
			"Magia_51": "303751_m_r",
			"Regular": "303700_r",
			"Regular_01": "303701_r",
			"Regular_09": "303709_r",
			"Regular_51": "303751_r",
		}
	}, {
		"NAME": "Midori Ryou",
		"ID": "3046",
		"ICON": "card_30463_d.png",
		"SKIN": {
			"Doppel": "304600_d_r",
			"Magia": "304600_m_r",
			"Magia_51": "304651_m_r",
			"Regular": "304600_r",
			"Regular_01": "304601_r",
			"Regular_09": "304609_r",
			"Regular_51": "304651_r"
		}
	}, {
		"NAME": "Yukino Kanae",
		"ID": "3049",
		"ICON": "card_30494_d.png",
		"SKIN": {
			"Doppel": "304900_d_r",
			"Magia": "304900_m_r",
			"Magia_51": "304951_m_r",
			"Regular": "304900_r",
			"Regular_09": "304909_r",
			"Regular_51": "304951_r",
		}
	}, {
		"NAME": "Nanami Yachiyo & Asuza Mifuyu",
		"ID": "1102",
		"ICON": "card_11024_d.png",
		"SKIN": {	
			"Doppel": "110200_d_r",
			"Magia": "110200_m_r",
			"Regular": "110200_r",	
			"Regular_09": "110209_r",
		}
	}, {
		"NAME": "Kazari Jun",
		"ID": "3051",
		"ICON": "card_30514_d.png",
		"SKIN": {	
			"Doppel": "305100_d_r",
			"Magia": "305100_m_r",
			"Regular": "305100_r",	
			"Regular_09": "305109_r",
		}
	}, {
		"NAME": "Tokime Shizuka",
		"ID": "1025",
		"ICON": "card_10254_d.png",
		"SKIN": {	
			"Doppel": "102500_d_r",
			"Magia": "102500_m_r",
			"Magia_50": "102550_m_r",
			"Regular": "102500_r",	
			"Regular_09": "102509_r",
			"Regular_50": "102550_r"
		}
	}, {
		"NAME": "Misaki Umika",
		"ID": "4012",
		"ICON": "card_40123_d.png",
		"SKIN": {
			"Doppel": "401200_d_r",
			"Magia": "401200_m_r",
			"Regular": "401200_r",
			"Regular_01": "401201_r",
			"Regular_09": "401209_r"
		}
	}, {
		"NAME": "Little Kyubey",
		"ID": "1042",
		"ICON": "card_10424_d.png",
		"SKIN": {	
			"Doppel": "104200_d_r",
			"Magia": "104200_m_r",
			"Regular": "104200_r",	
			"Regular_09": "104209_r",
		}
	},
	{
		"NAME": "Eternal Sakura Swimsuit ver.",
		"ID": "3502",
		"ICON": "card_35024_d.png",
		"SKIN": {	
			"Doppel": "350200_d_r",
			"Magia": "350200_m_r",
			"Magia_50": "350250_m_r",
			"Regular": "350200_r",	
			"Regular_09": "350209_r",
			"Regular_50": "350250_r"	
		}
	}, 
	{
		"NAME": "Tomoe Mami Swimsuit ver.",
		"ID": "2501",
		"ICON": "card_25014_d.png",
		"SKIN": {	
			"Doppel": "250100_d_r",
			"Magia": "250100_m_r",
			"Regular": "250100_r",	
			"Regular_09": "250109_r"	
		}
	}, 
	{
		"NAME": "Rena Kaede Swimsuit ver.",
		"ID": "1209",
		"ICON": "card_12094_d.png",
		"SKIN": {	
			"Doppel": "120900_d_r",
			"Magia": "120900_m_r",
			"Regular": "120900_r",	
			"Regular_09": "120909_r"	
		}
	}, 
	{
		"NAME": "Sawa Sudachi",
		"ID": "1039",
		"ICON": "card_10394_d.png",
		"SKIN": {	
			"Doppel": "103900_d_r",
			"Magia": "103900_m_r",
			"Regular": "103900_r",	
			"Regular_09": "103909_r"	
		}
	}, {
		"NAME": "Aoba Chika",
		"ID": "3047",
		"ICON": "card_30473_d.png",
		"SKIN": {
			"Magia": "304700_m_r",
			"Regular": "304700_r"
		}
	}, {
		"NAME": "Ashley Taylor",
		"ID": "3052",
		"ICON": "card_30524_d.png",
		"SKIN": {	
			"Doppel": "305200_d_r",
			"Magia": "305200_m_r",
			"Regular": "305200_r",	
			"Regular_09": "305209_r"	
		}
	}, {
		"NAME": "Tart Final ver.",
		"ID": "4028",
		"ICON": "card_40284_d.png",
		"SKIN": {
			"Doppel": "402800_d_r",
			"Magia": "402800_m_r",
			"Regular": "402800_r",
			"Regular_09": "402809_r"
		}
	}, {
		"NAME": "Lapine",
		"ID": "4027",
		"ICON": "card_40273_d.png",
		"SKIN": {
			"Doppel": "402700_d_r",
			"Magia": "402700_m_r",
			"Magia_50": "402750_m_r",
			"Regular": "402700_r",
			"Regular_01": "402701_r",
			"Regular_09": "402709_r",
			"Regular_50": "402750_r"
		}
	}, {
		"NAME": "Kaharu Yuuna",
		"ID": "3050",
		"ICON": "card_30504_d.png",
		"SKIN": {
			"Doppel": "305000_d_r",
			"Magia": "305000_m_r",
			"Regular": "305000_r",
			"Regular_09": "305009_r"
		}
	}, {
		"NAME": "Yuzuki Hotori",
		"ID": "3041",
		"ICON": "card_30413_d.png",
		"SKIN": {
			"Magia": "304100_m_r",
			"Regular": "304100_r"
		}
	}, {
		"NAME": "Yuzuki Rion",
		"ID": "3045",
		"ICON": "card_30453_d.png",
		"SKIN": {
			"Magia": "304500_m_r",
			"Regular": "304500_r"
		}
	}, {
		"NAME": "Wakana Tsumugi",
		"ID": "3024",
		"ICON": "card_30244_d.png",
		"SKIN": {
			"Doppel": "302400_d_r",
			"Magia": "302400_m_r",
			"Regular": "302400_r",
			"Regular_09": "302409_r"
		}
	}, {
		"NAME": "Kurumi Manaka",
		"ID": "3009",
		"ICON": "card_30092_d.png",
		"SKIN": {
			"Doppel": "300900_d_r",
			"Magia": "300900_m_r",
			"Regular": "300900_r",
			"Regular_01": "300901_r",
			"Regular_09": "300909_r"
		}
	}, {
		"NAME": "Mizuki Rui",
		"ID": "3056",
		"ICON": "card_30564_d.png",
		"SKIN": {
			"Doppel": "305600_d_r",
			"Magia": "305600_m_r",
			"Regular": "305600_r",
			"Regular_09": "305609_r"
		}
	}, {
		"NAME": "Yagami Hayate",
		"ID": "4053",
		"ICON": "card_40533_d.png",
		"SKIN": {
			"Doppel": "405300_d_r",
			"Magia": "405300_m_r",
			"Regular": "405300_r",
			"Regular_09": "405309_r"
		}
	}, {
		"NAME": "Kasane Ao",
		"ID": "1023",
		"ICON": "card_10234_d.png",
		"SKIN": {
			"Doppel": "102300_d_r",
			"Magia": "102300_m_r",
			"Magia_50": "102350_m_r",
			"Regular": "102300_r",
			"Regular_09": "102309_r",
			"Regular_50": "102350_r"
		}
	}, {
		"NAME": "Yuuki Maria",
		"ID": "3036",
		"ICON": "card_30363_d.png",
		"SKIN": {
			"Magia": "303600_m_r",
			"Regular": "303600_r"
		}
	}, {
		"NAME": "Rena-chan",
		"ID": "1109",
		"ICON": "card_11094_d.png",
		"SKIN": {
			"Doppel": "110900_d_r",
			"Magia": "110900_m_r",
			"Regular": "110900_r",
			"Regular_09": "110909_r"
		}
	}, {
		"NAME": "Felicia-chan",
		"ID": "1105",
		"ICON": "card_11053_d.png",
		"SKIN": {
			"Doppel": "110500_d_r",
			"Magia": "110500_m_r",
			"Regular": "110500_r",
			"Regular_09": "110509_r"
		}
	}, {
		"NAME": "Sasame Yozuru",
		"ID": "1040",
		"ICON": "card_10404_d.png",
		"SKIN": {
			"Doppel": "104000_d_r",
			"Magia": "104000_m_r",
			"Regular": "104000_r",
			"Regular_09": "104009_r"
		}
	}, {
		"NAME": "Hibiki Meguru",
		"ID": "3042",
		"ICON": "card_30423_d.png",
		"SKIN": {
			"Magia": "304200_m_r",
			"Regular": "304200_r"
		}
	}, {
		"NAME": "Azumi Hagumu",
		"ID": "1030",
		"ICON": "card_10304_d.png",
		"SKIN": {
			"Doppel": "103000_d_r",
			"Magia": "103000_m_r",
			"Magia_50": "103050_m_r",
			"Regular": "103000_r",
			"Regular_09": "103009_r",
			"Regular_50": "103050_r"
		}
	}, {
		"NAME": "Megumi Moka",
		"ID": "3034",
		"ICON": "card_30343_d.png",
		"SKIN": {
			"Magia": "303400_m_r",
			"Regular": "303400_r"
		}
	}, {
		"NAME": "Nagisa Momoe Valentines Ver.",
		"ID": "2700",
		"ICON": "card_27004_d.png",
		"SKIN": {
			"Doppel": "270000_d_r",
			"Magia": "270000_m_r",
			"Regular": "270000_r",
			"Regular_09": "270009_r"
		}
	}, {
		"NAME": "Minamitsu Ryouko",
		"ID": "3058",
		"ICON": "card_30584_d.png",
		"SKIN": {
			"Doppel": "305800_d_r",
			"Magia": "305800_m_r",
			"Regular": "305800_r",
			"Regular_09": "305809_r"
		}
	}, {
		"NAME": "Suzuka Sakuya",
		"ID": "3021",
		"ICON": "card_30212_d.png",
		"SKIN": {
			"Magia": "302100_m_r",
			"Regular": "302100_r"
		}
	}, {
		"NAME": "Livia Mediros",
		"ID": "1041",
		"ICON": "card_10414_d.png",
		"SKIN": {
			"Doppel": "104100_d_r",
			"Magia": "104100_m_r",
			"Regular": "104100_r",
			"Regular_09": "104109_r"
		}
	}, {
		"NAME": "Chitama Ranka",
		"ID": "3044",
		"ICON": "card_30443_d.png",
		"SKIN": {
			"Magia": "304400_m_r",
			"Regular": "304400_r"
		}
	}, {
		"NAME": "Miki Sayaka Haregi Ver.",
		"ID": "2400",
		"ICON": "card_24004_d.png",
		"SKIN": {
			"Doppel": "240000_d_r",
			"Magia": "240000_m_r",
			"Regular": "240000_r",
			"Regular_09": "240009_r"
		}
	}, {
		"NAME": "Ayano Rika & Isuzu Ren Christmas Ver.",
		"ID": "3501",
		"ICON": "card_35014_d.png",
		"SKIN": {
			"Doppel": "350100_d_r",
			"Magia": "350100_m_r",
			"Regular": "350100_r",
			"Regular_09": "350109_r"
		}
	}, {
		"NAME": "Miyabi Shigure",
		"ID": "1029",
		"ICON": "card_10294_d.png",
		"SKIN": {
			"Doppel": "102900_d_r",
			"Magia": "102900_m_r",
			"Magia_50": "102950_m_r",
			"Magia_51": "102951_m_r",
			"Regular": "102900_r",
			"Regular_09": "102909_r",
			"Regular_50": "102950_r",
			"Regular_51": "102951_r"
		}
	}, {
		"NAME": "Komachi Mikura",
		"ID": "3038",
		"ICON": "card_30384_d.png",
		"SKIN": {
			"Doppel": "303800_d_r",
			"Magia": "303800_m_r",
			"Regular": "303800_r",
			"Regular_09": "303809_r"
		}
	}, {
		"NAME": "Mihono Seira",
		"ID": "3039",
		"ICON": "card_30393_d.png",
		"SKIN": {
			"Magia": "303900_m_r",
			"Regular": "303900_r"
		}
	}, {
		"NAME": "Kira Temari",
		"ID": "3040",
		"ICON": "card_30403_d.png",
		"SKIN": {
			"Magia": "304000_m_r",
			"Regular": "304000_r"
		}
	}, {
		"NAME": "Kirari Hikaru",
		"ID": "1022",
		"ICON": "card_10224_d.png",
		"SKIN": {
			"Doppel": "102200_d_r",
			"Magia": "102200_m_r",
			"Magia_50": "102250_m_r",
			"Magia_51": "102251_m_r",
			"Regular": "102200_r",
			"Regular_09": "102209_r",
			"Regular_50": "102250_r",
			"Regular_51": "102251_r"
		}
	}, {
		"NAME": "Aino Mito",
		"ID": "3015",
		"ICON": "card_30154_d.png",
		"SKIN": {
			"Doppel": "301500_d_r",
			"Magia": "301500_m_r",
			"Regular": "301500_r",
			"Regular_09": "301509_r"
		}
	}, {
		"NAME": "Akemi Homura",
		"ID": "2002",
		"ICON": "card_20024_d.png",
		"SKIN": {
			"Doppel": "200200_d_r",
			"Magia": "200200_m_r",
			"Regular": "200200_r",
			"Regular_09": "200209_r"
		}
	}, {
		"NAME": "Akemi Homura Glasses Ver.",
		"ID": "2003",
		"ICON": "card_20034_d.png",
		"SKIN": {
			"Doppel": "200300_d_r",
			"Magia": "200300_m_r",
			"Regular": "200300_r",
			"Regular_01": "200301_r",
			"Regular_09": "200309_r"
		}
	}, {
		"NAME": "Akemi Homura Swimsuit Ver.",
		"ID": "2300",
		"ICON": "card_23004_d.png",
		"SKIN": {
			"Doppel": "230000_d_r",
			"Magia": "230000_m_r",
			"Regular": "230000_r",
			"Regular_09": "230009_r"
		}
	}, {
		"NAME": "Akino Kaede",
		"ID": "1011",
		"ICON": "card_10113_d.png",
		"SKIN": {
			"Doppel": "101100_d_r",
			"Magia": "101100_m_r",
			"Regular": "101100_r",
			"Regular_01": "101101_r",
			"Regular_09": "101109_r",
			"Regular_50": "101150_r"
		}
	}, {
		"NAME": "Alina Gray",
		"ID": "1008",
		"ICON": "card_10084_d.png",
		"SKIN": {
			"Doppel": "100800_d_r",
			"Doppel_50": "100850_d_r",
			"Doppel_51": "100851_d_r",
			"Magia": "100800_m_r",
			"Magia_50": "100850_m_r",
			"Regular": "100800_r",
			"Regular_09": "100809_r",
			"Regular_50": "100850_r",
			"Regular_51": "100851_r"
		}
	}, {
		"NAME": "Amane Tsukasa",
		"ID": "1019",
		"ICON": "card_10193_d.png",
		"SKIN": {
			"Doppel": "101900_d_r",
			"Magia": "101900_m_r",
			"Magia_50": "101950_m_r",
			"Regular": "101900_r",
			"Regular_01": "101901_r",
			"Regular_09": "101909_r",
			"Regular_51": "101951_r"
		}
	}, {
		"NAME": "Amane Tsukuyo",
		"ID": "1018",
		"ICON": "card_10184_d.png",
		"SKIN": {
			"Doppel": "101800_d_r",
			"Doppel_50": "101850_d_r",
			"Magia": "101800_m_r",
			"Magia_50": "101850_m_r",
			"Regular": "101800_r",
			"Regular_09": "101809_r",
			"Regular_50": "101850_r",
			"Regular_51": "101851_r"
		}
	}, {
		"NAME": "Amane Sisters Mizugi Ver.",
		"ID": "1118",
		"ICON": "card_11184_d.png",
		"SKIN": {
			"Doppel": "111800_d_r",
			"Magia": "111800_m_r",
			"Regular": "111800_r",
			"Regular_09": "111809_r"
		}
	}, {
		"NAME": "Amano Suzune",
		"ID": "4031",
		"ICON": "card_40314_d.png",
		"SKIN": {
			"Doppel": "403100_d_r",
			"Magia": "403100_m_r",
			"Magia_50": "403150_m_r",
			"Regular": "403100_r",
			"Regular_09": "403109_r",
			"Regular_50": "403150_r"
		}
	}, {
		"NAME": "Ami Ria",
		"ID": "3010",
		"ICON": "card_30103_d.png",
		"SKIN": {
			"Magia": "301000_m_r",
			"Regular": "301000_r"
		}
	}, {
		"NAME": "Awane Kokoro",
		"ID": "3016",
		"ICON": "card_30164_d.png",
		"SKIN": {
			"Doppel": "301600_d_r",
			"Magia": "301600_m_r",
			"Regular": "301600_r",
			"Regular_01": "301601_r",
			"Regular_09": "301609_r"
		}
	}, {
		"NAME": "Ayano Rika",
		"ID": "3031",
		"ICON": "card_30313_d.png",
		"SKIN": {
			"Magia": "303100_m_r",
			"Regular": "303100_r"
		}
	}, {
		"NAME": "Azusa Mifuyu",
		"ID": "1006",
		"ICON": "card_10064_d.png",
		"SKIN": {
			"Doppel": "100600_d_r",
			"Magia": "100600_m_r",
			"Regular": "100600_r",
			"Regular_09": "100609_r",
			"Regular_50": "100650_r"
		}
	}, {
		"NAME": "Chiaki Riko",
		"ID": "3035",
		"ICON": "card_30354_d.png",
		"SKIN": {
			"Doppel": "303500_d_r",
			"Magia": "303500_m_r",
			"Regular": "303500_r",
			"Regular_09": "303509_r"
		}
	}, {
		"NAME": "Chitose Yuma",
		"ID": "4003",
		"ICON": "card_40033_d.png",
		"SKIN": {
			"Doppel": "400300_d_r",
			"Magia": "400300_m_r",
			"Regular": "400300_r",
			"Regular_01": "400301_r",
			"Regular_09": "400309_r"
		}
	}, {
		"NAME": "Chun Meiyun",
		"ID": "3012",
		"ICON": "card_30123_d.png",
		"SKIN": {
			"Doppel": "301200_d_r",
			"Magia": "301200_m_r",
			"Regular": "301200_r",
			"Regular_01": "301201_r",
			"Regular_09": "301209_r"
		}
	}, {
		"NAME": "Corbeau",
		"ID": "4025",
		"ICON": "card_40253_d.png",
		"SKIN": {
			"Doppel": "402500_d_r",
			"Magia": "402500_m_r",
			"Magia_50": "402550_m_r",
			"Magia_51": "402551_m_r",
			"Regular": "402500_r",
			"Regular_01": "402501_r",
			"Regular_09": "402509_r",
			"Regular_50": "402550_r",
			"Regular_51": "402551_r"
		}
	}, {
		"NAME": "Elisa Celjska",
		"ID": "4026",
		"ICON": "card_40264_d.png",
		"SKIN": {
			"Doppel": "402600_d_r",
			"Magia": "402600_m_r",
			"Regular": "402600_r",
			"Regular_09": "402609_r"
		}
	}, {
		"NAME": "Eri Aimi",
		"ID": "3023",
		"ICON": "card_30233_d.png",
		"SKIN": {
			"Magia": "302300_m_r",
			"Regular": "302300_r"
		}
	}, {
		"NAME": "Fate T. Harlaown",
		"ID": "4052",
		"ICON": "card_40524_d.png",
		"SKIN": {
			"Doppel": "405200_d_r",
			"Magia": "405200_m_r",
			"Regular": "405200_r",
			"Regular_09": "405209_r"
		}
	}, {
		"NAME": "Fumino Sayuki",
		"ID": "3033",
		"ICON": "card_30334_d.png",
		"SKIN": {
			"Doppel": "303300_d_r",
			"Magia": "303300_m_r",
			"Regular": "303300_r",
			"Regular_09": "303309_r"
		}
	}, {
		"NAME": "Futaba Sana",
		"ID": "1004",
		"ICON": "card_10042_d.png",
		"SKIN": {
			"Doppel": "100400_d_r",
			"Doppel_01": "100401_d_r",
			"Magia": "100400_m_r",
			"Regular": "100400_r",
			"Regular_01": "100401_r",
			"Regular_09": "100409_r"
		}
	}, {
		"NAME": "Hachikuji Mayoi",
		"ID": "4042",
		"ICON": "card_40424_d.png",
		"SKIN": {
			"Doppel": "404200_d_r",
			"Magia": "404200_m_r",
			"Regular": "404200_r",
			"Regular_09": "404209_r"
		}
	}, {
		"NAME": "Hanekawa Tsubasa",
		"ID": "4045",
		"ICON": "card_40454_d.png",
		"SKIN": {
			"Doppel": "404500_d_r",
			"Magia": "404500_m_r",
			"Regular": "404500_r",
			"Regular_09": "404509_r"
		}
	}, {
		"NAME": "Haruna Konomi",
		"ID": "3030",
		"ICON": "card_30302_d.png",
		"SKIN": {
			"Doppel": "303000_d_r",
			"Magia": "303000_m_r",
			"Regular": "303000_r",
			"Regular_01": "303001_r",
			"Regular_09": "303009_r"
		}
	}, {
		"NAME": "Hiiragi Nemu",
		"ID": "1014",
		"ICON": "card_10144_d.png",
		"SKIN": {
			"Doppel": "101400_d_r",
			"Doppel_51": "101451_d_r",
			"Magia": "101400_m_r",
			"Magia_50": "101450_m_r",
			"Regular": "101400_r",
			"Regular_09": "101409_r",
			"Regular_50": "101450_r",
			"Regular_51": "101451_r"
		}
	}, {
		"NAME": "Hinata Matsuri",
		"ID": "4032",
		"ICON": "card_40324_d.png",
		"SKIN": {
			"Doppel": "403200_d_r",
			"Magia": "403200_m_r",
			"Regular": "403200_r",
			"Regular_09": "403209_r"
		}
	}, {
		"NAME": "Hiroe Chiharu",
		"ID": "1026",
		"ICON": "card_10264_d.png",
		"SKIN": {
			"Doppel": "102600_d_r",
			"Magia": "102600_m_r",
			"Regular": "102600_r",
			"Regular_09": "102609_r"
		}
	}, {
		"NAME": "Holy Alina",
		"ID": "1108",
		"ICON": "card_11084_d.png",
		"SKIN": {
			"Doppel": "110800_d_r",
			"Magia": "110800_m_r",
			"Regular": "110800_r",
			"Regular_09": "110809_r"
		}
	}, {
		"NAME": "Holy Alina Final Form",
		"ID": "1208",
		"ICON": "card_12081_d.png",
		"SKIN": {
			"Magia": "120850_m_r",
			"Regular": "120850_r"
		}
	}, {
		"NAME": "Holy Mami",
		"ID": "2500",
		"ICON": "card_25004_d.png",
		"SKIN": {
			"Doppel": "250000_d_r",
			"Doppel_50": "250050_d_r",
			"Magia": "250000_m_r",
			"Magia_50": "250050_m_r",
			"Regular": "250000_r",
			"Regular_09": "250009_r",
			"Regular_50": "250050_r"
		}
	}, {
		"NAME": "Rumor of the Kamihama Saint",
		"ID": "6500",
		"ICON": "card_65014_d.png",
		"SKIN": {
			"Magia": "650100_m_r",
			"Regular": "650100_r"
		}
	}, {
		"NAME": "Hozumi Shizuku",
		"ID": "3007",
		"ICON": "card_30072_d.png",
		"SKIN": {
			"Magia": "300700_m_r",
			"Regular": "300700_r",
			"Regular_09": "300709_r"
		}
	}, {
		"NAME": "Ibuki Reira",
		"ID": "3013",
		"ICON": "card_30133_d.png",
		"SKIN": {
			"Magia": "301300_m_r",
			"Regular": "301300_r"
		}
	}, {
		"NAME": "Iroha-chan",
		"ID": "1201",
		"ICON": "card_12014_d.png",
		"SKIN": {
			"Doppel": "120100_d_r",
			"Magia": "120100_m_r",
			"Regular": "120100_r",
			"Regular_09": "120109_r"
		}
	}, {
		"NAME": "Isuzu Ren",
		"ID": "3025",
		"ICON": "card_30254_d.png",
		"SKIN": {
			"Doppel": "302500_d_r",
			"Magia": "302500_m_r",
			"Regular": "302500_r",
			"Regular_01": "302501_r",
			"Regular_09": "302509_r"
		}
	}, {
		"NAME": "Izumi Kanagi",
		"ID": "1016",
		"ICON": "card_10164_d.png",
		"SKIN": {
			"Doppel": "101600_d_r",
			"Magia": "101600_m_r",
			"Regular": "101600_r",
			"Regular_09": "101609_r"
		}
	}, {
		"NAME": "Kagami Masara",
		"ID": "3029",
		"ICON": "card_30293_d.png",
		"SKIN": {
			"Doppel": "302900_d_r",
			"Magia": "302900_m_r",
			"Regular": "302900_r",
			"Regular_01": "302901_r",
			"Regular_09": "302909_r"
		}
	}, {
		"NAME": "Kanade Haruka",
		"ID": "4035",
		"ICON": "card_40353_d.png",
		"SKIN": {
			"Doppel": "403500_d_r",
			"Magia": "403500_m_r",
			"Regular": "403500_r",
			"Regular_01": "403501_r",
			"Regular_09": "403509_r"
		}
	}, {
		"NAME": "Kaname Madoka",
		"ID": "2001",
		"ICON": "card_20014_d.png",
		"SKIN": {
			"Doppel": "200100_d_r",
			"Magia": "200100_m_r",
			"Regular": "200100_r",
			"Regular_09": "200109_r"
		}
	}, {
		"NAME": "Kaname Madoka Haregi Ver.",
		"ID": "2100",
		"ICON": "card_21004_d.png",
		"SKIN": {
			"Doppel": "210000_d_r",
			"Magia": "210000_m_r",
			"Regular": "210000_r",
			"Regular_09": "210009_r"
		}
	}, {
		"NAME": "Kanbaru Suruga",
		"ID": "4043",
		"ICON": "card_40434_d.png",
		"SKIN": {
			"Doppel": "404300_d_r",
			"Magia": "404300_m_r",
			"Regular": "404300_r",
			"Regular_09": "404309_r"
		}
	}, {
		"NAME": "Kazumi",
		"ID": "4011",
		"ICON": "card_40114_d.png",
		"SKIN": {
			"Doppel": "401100_d_r",
			"Magia": "401100_m_r",
			"Regular": "401100_r",
			"Regular_09": "401109_r"
		}
	}, {
		"NAME": "Kisaki Emiri",
		"ID": "3006",
		"ICON": "card_30062_d.png",
		"SKIN": {
			"Doppel": "300600_d_r",
			"Magia": "300600_m_r",
			"Regular": "300600_r",
			"Regular_01": "300601_r",
			"Regular_09": "300609_r"
		}
	}, {
		"NAME": "Kozue Mayu",
		"ID": "3032",
		"ICON": "card_30324_d.png",
		"SKIN": {
			"Doppel": "303200_d_r",
			"Magia": "303200_m_r",
			"Regular": "303200_r",
			"Regular_09": "303209_r"
		}
	}, {
		"NAME": "Kumi Seika",
		"ID": "3014",
		"ICON": "card_30142_d.png",
		"SKIN": {
			"Magia": "301400_m_r",
			"Regular": "301400_r"
		}
	}, {
		"NAME": "Kure Kirika",
		"ID": "4002",
		"ICON": "card_40024_d.png",
		"SKIN": {
			"Doppel": "400200_d_r",
			"Magia": "400200_m_r",
			"Regular": "400200_r",
			"Regular_09": "400209_r"
		}
	}, {
		"NAME": "Kuro",
		"ID": "3900",
		"ICON": "card_39001_d.png",
		"SKIN": {
			"Magia": "390000_m_r",
			"Regular": "390000_r"
		}
	}, {
		"NAME": "Madoka-senpai",
		"ID": "2102",
		"ICON": "card_21023_d.png",
		"SKIN": {
			"Doppel": "210200_d_r",
			"Magia": "210200_m_r",
			"Regular": "210200_r",
			"Regular_09": "210209_r"
		}
	}, {
		"NAME": "Maki Kaoru",
		"ID": "4013",
		"ICON": "card_40133_d.png",
		"SKIN": {
			"Doppel": "401300_d_r",
			"Magia": "401300_m_r",
			"Regular": "401300_r",
			"Regular_01": "401301_r",
			"Regular_09": "401309_r"
		}
	}, {
		"NAME": "Makino Ikumi",
		"ID": "3053",
		"ICON": "card_30534_d.png",
		"SKIN": {
			"Doppel": "305300_d_r",
			"Magia": "305300_m_r",
			"Regular": "305300_r",
			"Regular_09": "305309_r"
		}
	}, {
		"NAME": "Mao Himika",
		"ID": "3020",
		"ICON": "card_30204_d.png",
		"SKIN": {
			"Doppel": "302000_d_r",
			"Magia": "302000_m_r",
			"Regular": "302000_r",
			"Regular_09": "302009_r"
		}
	}, {
		"NAME": "Mariko Ayaka",
		"ID": "3019",
		"ICON": "card_30193_d.png",
		"SKIN": {
			"Magia": "301900_m_r",
			"Regular": "301900_r"
		}
	}, {
		"NAME": "Melissa de Vignolles",
		"ID": "4023",
		"ICON": "card_40233_d.png",
		"SKIN": {
			"Doppel": "402300_d_r",
			"Magia": "402300_m_r",
			"Regular": "402300_r",
			"Regular_09": "402309_r"
		}
	}, {
		"NAME": "Miki Sayaka",
		"ID": "2004",
		"ICON": "card_20044_d.png",
		"SKIN": {
			"Doppel": "200400_d_r",
			"Magia": "200400_m_r",
			"Regular": "200400_r",
			"Regular_09": "200409_r"
		}
	}, {
		"NAME": "Mikuni Oriko",
		"ID": "4001",
		"ICON": "card_40013_d.png",
		"SKIN": {
			"Doppel": "400100_d_r",
			"Magia": "400100_m_r",
			"Regular": "400100_r",
			"Regular_01": "400101_r",
			"Regular_09": "400109_r"
		}
	}, {
		"NAME": "Mikuri Ayame",
		"ID": "3028",
		"ICON": "card_30282_d.png",
		"SKIN": {
			"Doppel": "302800_d_r",
			"Magia": "302800_m_r",
			"Regular": "302800_r",
			"Regular_01": "302801_r",
			"Regular_09": "302809_r"
		}
	}, {
		"NAME": "Minagi Sasara",
		"ID": "3004",
		"ICON": "card_30042_d.png",
		"SKIN": {
			"Doppel": "300400_d_r",
			"Magia": "300400_m_r",
			"Regular": "300400_r",
			"Regular_01": "300401_r",
			"Regular_09": "300409_r"
		}
	}, {
		"NAME": "Minami Rena",
		"ID": "1009",
		"ICON": "card_10094_d.png",
		"SKIN": {
			"Doppel": "100900_d_r",
			"Magia": "100900_m_r",
			"Regular": "100900_r",
			"Regular_09": "100909_r",
			"Regular_50": "100950_r"
		}
	}, {
		"NAME": "Minou",
		"ID": "4024",
		"ICON": "card_40244_d.png",
		"SKIN": {
			"Doppel": "402400_d_r",
			"Magia": "402400_m_r",
			"Regular": "402400_r",
			"Regular_09": "402409_r",
			"Regular_50": "402450_r",
			"Regular_51": "402451_r"
		}
	}, {
		"NAME": "Misono Karin",
		"ID": "1012",
		"ICON": "card_10124_d.png",
		"SKIN": {
			"Doppel": "101200_d_r",
			"Magia": "101200_m_r",
			"Regular": "101200_r",
			"Regular_09": "101209_r"
		}
	}, {
		"NAME": "Mitsuki Felicia",
		"ID": "1005",
		"ICON": "card_10052_d.png",
		"SKIN": {
			"Doppel": "100500_d_r",
			"Magia": "100500_m_r",
			"Regular": "100500_r",
			"Regular_01": "100501_r",
			"Regular_09": "100509_r"
		}
	}, {
		"NAME": "Miyako Hinano",
		"ID": "3003",
		"ICON": "card_30032_d.png",
		"SKIN": {
			"Doppel": "300300_d_r",
			"Magia": "300300_m_r",
			"Regular": "300300_r",
			"Regular_01": "300301_r",
			"Regular_09": "300309_r"
		}
	}, {
		"NAME": "Momoe Nagisa",
		"ID": "2007",
		"ICON": "card_20074_d.png",
		"SKIN": {
			"Doppel": "200700_d_r",
			"Magia": "200700_m_r",
			"Regular": "200700_r",
			"Regular_09": "200709_r"
		}
	}, {
		"NAME": "Nanase Yukika",
		"ID": "3017",
		"ICON": "card_30174_d.png",
		"SKIN": {
			"Doppel": "301700_d_r",
			"Magia": "301700_m_r",
			"Regular": "301700_r",
			"Regular_09": "301709_r"
		}
	}, {
		"NAME": "Narumi Arisa",
		"ID": "4033",
		"ICON": "card_40333_d.png",
		"SKIN": {
			"Doppel": "403300_d_r",
			"Magia": "403300_m_r",
			"Regular": "403300_r",
			"Regular_01": "403301_r",
			"Regular_09": "403309_r"
		}
	}, {
		"NAME": "Natsume Kako",
		"ID": "3011",
		"ICON": "card_30112_d.png",
		"SKIN": {
			"Doppel": "301100_d_r",
			"Magia": "301100_m_r",
			"Regular": "301100_r",
			"Regular_01": "301101_r",
			"Regular_09": "301109_r"
		}
	}, {
		"NAME": "Oshino Shinobu",
		"ID": "4046",
		"ICON": "card_40464_d.png",
		"SKIN": {
			"Doppel": "404600_d_r",
			"Magia": "404600_m_r",
			"Regular": "404600_r",
			"Regular_09": "404609_r"
		}
	}, {
		"NAME": "Riz Hawkwood",
		"ID": "4022",
		"ICON": "card_40224_d.png",
		"SKIN": {
			"Doppel": "402200_d_r",
			"Magia": "402200_m_r",
			"Magia_50": "402250_m_r",
			"Regular": "402200_r",
			"Regular_09": "402209_r",
			"Regular_50": "402250_r"
		}
	}, {
		"NAME": "Eternal Sakura",
		"ID": "3043",
		"ICON": "card_30434_d.png",
		"SKIN": {
			"Doppel": "304300_d_r",
			"Magia": "304300_m_r",
			"Regular": "304300_r",
			"Regular_09": "304309_r"
		}
	}, {
		"NAME": "Sakura Kyouko",
		"ID": "2006",
		"ICON": "card_20064_d.png",
		"SKIN": {
			"Doppel": "200600_d_r",
			"Magia": "200600_m_r",
			"Regular": "200600_r",
			"Regular_09": "200609_r"
		}
	}, {
		"NAME": "Sakura Kyouko Swimsuit Ver.",
		"ID": "2600",
		"ICON": "card_26004_d.png",
		"SKIN": {
			"Doppel": "260000_d_r",
			"Magia": "260000_m_r",
			"Regular": "260000_r",
			"Regular_09": "260009_r"
		}
	}, {
		"NAME": "Sasara Hanna",
		"ID": "3018",
		"ICON": "card_30184_d.png",
		"SKIN": {
			"Doppel": "301800_d_r",
			"Magia": "301800_m_r",
			"Magia_50": "301850_m_r",
			"Regular": "301800_r",
			"Regular_09": "301809_r",
			"Regular_50": "301850_r"
		}
	}, {
		"NAME": "Satomi Touka",
		"ID": "1007",
		"ICON": "card_10074_d.png",
		"SKIN": {
			"Doppel": "100700_d_r",
			"Doppel_50": "100750_d_r",
			"Magia": "100700_m_r",
			"Magia_50": "100750_m_r",
			"Regular": "100700_r",
			"Regular_09": "100709_r",
			"Regular_50": "100750_r",
			"Regular_51": "100751_r"
		}
	}, {
		"NAME": "Sengoku Nadeko",
		"ID": "4044",
		"ICON": "card_40444_d.png",
		"SKIN": {
			"Doppel": "404400_d_r",
			"Magia": "404400_m_r",
			"Regular": "404400_r",
			"Regular_09": "404409_r"
		}
	}, {
		"NAME": "Senjougahara Hitagi",
		"ID": "4041",
		"ICON": "card_40413_d.png",
		"SKIN": {
			"Doppel": "404100_d_r",
			"Magia": "404100_m_r",
			"Regular": "404100_r",
			"Regular_01": "404101_r",
			"Regular_09": "404109_r"
		}
	}, {
		"NAME": "Shinobu Akira",
		"ID": "3008",
		"ICON": "card_30082_d.png",
		"SKIN": {
			"Doppel": "300800_d_r",
			"Magia": "300800_m_r",
			"Regular": "300800_r",
			"Regular_01": "300801_r",
			"Regular_09": "300809_r"
		}
	}, {
		"NAME": "Shion Chisato",
		"ID": "4034",
		"ICON": "card_40343_d.png",
		"SKIN": {
			"Magia": "403400_m_r",
			"Regular": "403400_r"
		}
	}, {
		"NAME": "Shizumi Konoha",
		"ID": "3026",
		"ICON": "card_30264_d.png",
		"SKIN": {
			"Doppel": "302600_d_r",
			"Magia": "302600_m_r",
			"Regular": "302600_r",
			"Regular_09": "302609_r"
		}
	}, {
		"NAME": "Takamachi Nanoha",
		"ID": "4051",
		"ICON": "card_40514_d.png",
		"SKIN": {
			"Doppel": "405100_d_r",
			"Magia": "405100_m_r",
			"Regular": "405100_r",
			"Regular_09": "405109_r"
		}
	}, {
		"NAME": "Tamaki Iroha",
		"ID": "1001",
		"ICON": "card_10011_d.png",
		"SKIN": {
			"Doppel": "100100_d_r",
			"Magia": "100100_m_r",
			"Regular": "100100_r",
			"Magia_01": "100101_m_r",
			"Regular_01": "100101_r",
			"Regular_09": "100109_r"
		}
	}, {
		"NAME": "Tamaki Iroha Swimsuit Ver.",
		"ID": "1101",
		"ICON": "card_11014_d.png",
		"SKIN": {
			"Doppel": "110100_d_r",
			"Magia": "110100_m_r",
			"Regular": "110100_r",
			"Regular_09": "110109_r"
		}
	}, {
		"NAME": "Tamaki Ui",
		"ID": "1015",
		"ICON": "card_10154_d.png",
		"SKIN": {
			"Doppel": "101500_d_r",
			"Magia": "101500_m_r",
			"Regular": "101500_r",
			"Regular_09": "101509_r"
		}
	}, {
		"NAME": "Tart",
		"ID": "4021",
		"ICON": "card_40214_d.png",
		"SKIN": {
			"Doppel": "402100_d_r",
			"Magia": "402100_m_r",
			"Regular": "402100_r",
			"Regular_09": "402109_r"
		}
	}, {
		"NAME": "Tatsuki Asuka",
		"ID": "1013",
		"ICON": "card_10133_d.png",
		"SKIN": {
			"Doppel": "101300_d_r",
			"Magia": "101300_m_r",
			"Regular": "101300_r",
			"Regular_01": "101301_r",
			"Regular_09": "101309_r"
		}
	}, {
		"NAME": "Togame Momoko",
		"ID": "1010",
		"ICON": "card_10104_d.png",
		"SKIN": {
			"Doppel": "101000_d_r",
			"Magia": "101000_m_r",
			"Regular": "101000_r",
			"Regular_09": "101009_r"
		}
	}, {
		"NAME": "Toki Sunao",
		"ID": "1027",
		"ICON": "card_10274_d.png",
		"SKIN": {
			"Doppel": "102700_d_r",
			"Magia": "102700_m_r",
			"Regular": "102700_r",
			"Regular_09": "102709_r"
		}
	}, {
		"NAME": "Tokiwa Nanaka",
		"ID": "3005",
		"ICON": "card_30053_d.png",
		"SKIN": {
			"Doppel": "300500_d_r",
			"Magia": "300500_m_r",
			"Regular": "300500_r",
			"Regular_01": "300501_r",
			"Regular_09": "300509_r"
		}
	}, {
		"NAME": "Tomoe Mami",
		"ID": "2005",
		"ICON": "card_20054_d.png",
		"SKIN": {
			"Doppel": "200500_d_r",
			"Magia": "200500_m_r",
			"Magia_50": "200550_m_r",
			"Regular": "200500_r",
			"Regular_09": "200509_r",
			"Regular_50": "200550_r"
		}
	}, {
		"NAME": "Ultimate Madoka",
		"ID": "2101",
		"ICON": "card_21014_d.png",
		"SKIN": {
			"Doppel": "210100_d_r",
			"Magia": "210100_m_r",
			"Regular": "210100_r",
			"Regular_09": "210109_r"
		}
	}, {
		"NAME": "Utsuho Natsuki",
		"ID": "3002",
		"ICON": "card_30022_d.png",
		"SKIN": {
			"Doppel": "300200_d_r",
			"Magia": "300200_m_r",
			"Regular": "300200_r",
			"Regular_01": "300201_r",
			"Regular_09": "300209_r"
		}
	}, {
		"NAME": "Yachiyo Nanami",
		"ID": "1002",
		"ICON": "card_10022_d.png",
		"SKIN": {
			"Doppel": "100200_d_r",
			"Magia": "100200_m_r",
			"Regular": "100200_r",
			"Regular_01": "100201_r",
			"Regular_09": "100209_r",
			"Regular_50": "100250_r"
		}
	}, {
		"NAME": "Yachiyo Nanami & Tamaki Iroha",
		"ID": "1301",
		"ICON": "card_13014_d.png",
		"SKIN": {
			"Doppel": "130100_d_r",
			"Magia": "130100_m_r",
			"Regular": "130100_r",
			"Regular_09": "130109_r"
		}
	}, {
		"NAME": "Yakumo Mitama",
		"ID": "1017",
		"ICON": "card_10174_d.png",
		"SKIN": {
			"Doppel": "101700_d_r",
			"Magia": "101700_m_r",
			"Regular": "101700_r",
			"Regular_09": "101709_r"
		}
	}, {
		"NAME": "Yakumo Mitama Haregi Ver.",
		"ID": "1117",
		"ICON": "card_11174_d.png",
		"SKIN": {
			"Doppel": "111700_d_r",
			"Magia": "111700_m_r",
			"Regular": "111700_r",
			"Regular_09": "111709_r"
		}
	}, {
		"NAME": "Yayoi Kanoko",
		"ID": "3001",
		"ICON": "card_30012_d.png",
		"SKIN": {
			"Doppel": "300100_d_r",
			"Magia": "300100_m_r",
			"Regular": "300100_r",
			"Regular_01": "300101_r",
			"Regular_09": "300109_r"
		}
	}, {
		"NAME": "Yui Tsuruno",
		"ID": "1003",
		"ICON": "card_10032_d.png",
		"SKIN": {
			"Doppel": "100300_d_r",
			"Magia": "100300_m_r",
			"Regular": "100300_r",
			"Regular_01": "100301_r",
			"Regular_09": "100309_r"
		}
	},{
		"NAME": "Yui Tsuruno Rumor Ver.",
		"ID": "1103",
		"ICON": "card_11033_d.png",
		"SKIN": {
			"Doppel": "110300_d_r",
			"Magia": "110300_m_r",
			"Magia_50": "110350_m_r",
			"Regular": "110300_r",
			"Regular_01": "110301_r",
			"Regular_09": "110309_r",
			"Regular_50": "110350_r"
		}
	}, {
		"NAME": "Yusa Hazuki",
		"ID": "3027",
		"ICON": "card_30273_d.png",
		"SKIN": {
			"Doppel": "302700_d_r",
			"Magia": "302700_m_r",
			"Regular": "302700_r",
			"Regular_01": "302701_r",
			"Regular_09": "302709_r"
		}
		// START WITCHES/UWASA/KIMOCHI
		// START FAMILIARS
		// START HUMANOID MOBS
	}, {
		"NAME": "Black Feather (Low)",
		"ID": "7150",
		"ICON": "card_71501_d.png",
		"SKIN": {
			"mini_715000_l": "715000_l",
			"mini_715001_l": "715001_l",
			"mini_715002_l": "715002_l",
			"mini_715003_l": "715003_l",
			"mini_715004_l": "715004_l",
			"mini_715005_l": "715005_l",
			"mini_715006_l": "715006_l",
			"mini_715007_l": "715007_l",
			"mini_715008_l": "715008_l",
			"mini_715009_l": "715009_l",
			"mini_715010_l": "715010_l",
			"mini_715011_l": "715011_l",
			"mini_715012_l": "715012_l",
			"mini_715013_l": "715013_l",
			"mini_715014_l": "715014_l",
			"mini_715015_l": "715015_l",
			"mini_715016_l": "715016_l",
			"mini_715017_l": "715017_l",
			"mini_715018_l": "715018_l",
			"mini_715019_l": "715019_l"
		}
	}, {
		"NAME": "White Feather (Low)",
		"ID": "7151",
		"ICON": "card_71511_d.png",
		"SKIN": {
			"mini_715100_l": "715100_l",
			"mini_715101_l": "715101_l",
			"mini_715102_l": "715102_l",
			"mini_715103_l": "715103_l",
			"mini_715104_d_l": "715104_d_l",
			"mini_715104_l": "715104_l",
			"mini_715105_l": "715105_l",
			"mini_715106_l": "715106_l",
			"mini_715107_l": "715107_l",
			"mini_715108_l": "715108_l",
			"mini_715109_l": "715109_l"
		}
	}, {
		"NAME": "General Soldier 1",
		"ID": "7600",
		"ICON": "card_76001_d.png",
		"SKIN": {
			"mini_760000_l": "760000_l"
		}
	}, {
		"NAME": "General Soldier 3",
		"ID": "7601",
		"ICON": "card_76011_d.png",
		"SKIN": {
			"mini_760100_l": "760100_l"
		}
	}, {
		"NAME": "Futatsugi City - Magical Girl",
		"ID": "7700",
		"ICON": "card_77001_d.png",
		"SKIN": {
			"mini_770000_l": "770000_l",
			"mini_770001_l": "770001_l",
			"mini_770002_l": "770002_l"
		}
	}, {
		"NAME": "Futatsugi City - General",
		"ID": "7701",
		"ICON": "card_77011_d.png",
		"SKIN": {
			"mini_770100_l": "770100_l",
			"mini_770101_l": "770101_l"
		}
	}, {
		"NAME": "Tokime Clan - Magical Girl",
		"ID": "7702",
		"ICON": "card_77021_d.png",
		"SKIN": {
			"mini_770200_l": "770200_l",
			"mini_770201_l": "770201_l",
			"mini_770202_l": "770202_l"
		}
	}, {
		"NAME": "Tokime Clan - General",
		"ID": "7703",
		"ICON": "card_77031_d.png",
		"SKIN": {
			"mini_770300_l": "770300_l",
			"mini_770301_l": "770301_l"
		}
	}, {
		"NAME": "Black Feather - Neo-Magius",
		"ID": "7704",
		"ICON": "card_77040_d.png",
		"SKIN": {
			"mini_770400_l": "770400_l",
			"mini_770401_l": "770401_l",
			"mini_770402_l": "770402_l"
		}
	}, {
		"NAME": "White Feather - Neo-Magius",
		"ID": "7705",
		"ICON": "card_77050_d.png",
		"SKIN": {
			"mini_770500_l": "770500_l",
			"mini_770501_l": "770501_l"
		}
	}, {
		"NAME": "Magical Girl",
		"ID": "7706",
		"ICON": "card_77061_d.png",
		"SKIN": {
			"mini_770600_l": "770600_l"
		}
	}, {
		"NAME": "Ai",
		"ID": "6005",
		"ICON": "card_60050_i.png",
		"SKIN": {
			"Regular": "600500_l",
		}
	}];
	
	CharData.sort((a, b) => (a.ID > b.ID) ? 1 : -1);