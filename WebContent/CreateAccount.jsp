<html>
    <head>
        <meta charset="UTF-8">
        <title>Create account</title>

        <link rel="stylesheet" type="text/css" href="js_lib/css/lib/control/iconselect.css" >
        <script type="text/javascript" src="js_lib/control/iconselect.js"></script>
        
        <script type="text/javascript" src="js_lib/iscroll.js"></script>
    	<link href="login.css" rel="stylesheet" type="text/css">
        
    </head>
    <body>

        <div class = "limiter">
			<div class = "container">
				<div class = "wrap">
			        <form action="AccountCreationServlet" method="post">
			        	<span class = "form-title">Create new Account.</span>
			    		<div class = "wrap-input">
							User Name: <input class = "input" type="text" name="userName" required="required" pattern="[A-Za-z0-9]{1,20}">
						</div>
						<div class = "wrap-input">
							Password: <input class = "input" type="password" name="password" required="required" pattern="[A-Za-z0-9]{1,20}">
						</div>
						<input id = "avatar_id_field" type = "hidden" name = "avatar_id" value = "">
						<div>
							
							<div style = "display:inline-block;right-padding:25px">
								<div  class="container-form-button">
									<div class="wrap-form-button">
										<div class="form-button"></div>
										<input  class = "button" type="submit" onclick = "getAvatarID()" value="Sign Up">
									</div>
								</div>
							</div>
							<div style = "display:inline-block;text-align:center;" >
								<div id="my-icon-select"></div>
							</div>	
						</div>
						
					</form>
				</div>
			</div>
		</div>

        <script>
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
        </script>

     
     <script>
     	function getAvatarID(){
			document.getElementById('avatar_id_field').value = iconSelect.getSelectedValue();
		};
	</script>

    </body>
</html>
