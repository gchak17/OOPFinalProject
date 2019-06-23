var iconSelect;

window.onload = function(){

    iconSelect = new IconSelect("my-icon-select", 
        {'selectedIconWidth':48,
        'selectedIconHeight':48,
        'selectedBoxPadding':5,
        'iconsWidth':48,
        'iconsHeight':48,
        'boxIconSpace':3,
        'vectoralIconNumber':2,
        'horizontalIconNumber':2});

    var icons = [];
    icons.push({'iconFilePath':'avatars/1.png', 'iconValue':'1'});
    icons.push({'iconFilePath':'avatars/2.png', 'iconValue':'2'});
    icons.push({'iconFilePath':'avatars/3.png', 'iconValue':'3'});
    icons.push({'iconFilePath':'avatars/4.png', 'iconValue':'4'});
    icons.push({'iconFilePath':'avatars/5.png', 'iconValue':'5'});
    icons.push({'iconFilePath':'avatars/6.png', 'iconValue':'6'});
    icons.push({'iconFilePath':'avatars/7.png', 'iconValue':'7'});
    icons.push({'iconFilePath':'avatars/8.png', 'iconValue':'8'});
    icons.push({'iconFilePath':'avatars/9.png', 'iconValue':'9'});
    
    iconSelect.refresh(icons);
};