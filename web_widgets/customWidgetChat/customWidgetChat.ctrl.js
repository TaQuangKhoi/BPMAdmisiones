function ($scope) {
     $(function(){
        window.$crisp=[];
        window.CRISP_WEBSITE_ID="5760f1e1-1fbb-432c-ae13-f743ecb1d17f";
        (
            function(){
                d=document;
                s=d.createElement("script");
                s.src="https://client.crisp.chat/l.js";
                s.async=1;
                d.getElementsByTagName("head")[0].appendChild(s);
            }
        )();
   });
}