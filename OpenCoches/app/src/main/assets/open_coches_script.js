/*
 * Función sacada de FC para poder ver vídeos
 */
function gen_output(video_content,randomid){
  document.getElementById(randomid).innerHTML = video_content;
}

/*
 * Función sacada de FC para poder ver vídeos
 */
function verVideo(videotag,randomid) {
      var registrado = 1;

      if ((registrado != 1) && (4913921 > 2000000)) {
        gen_output("<div align='center' class='video-youtube'><div class='video-container'><iframe type='text/html' width='640' height='360' src='http://widget.smartycenter.com/webservice/directYoutube/8982/" + videotag + "/640/360' frameborder='0' allowFullScreen scrolling='no'></iframe></div></div>",randomid);
      }

      else {
        gen_output("<div align='center' class='video-youtube'><div class='video-container'><iframe title='YouTube video player' class='youtube-player' type='text/html' width='640' height='390' src='//www.youtube.com/embed/" + videotag + "' frameborder='0' allowFullScreen></iframe></div></div>",randomid);
      }
}

/*
 * Función sacada de FC para poder ver vídeos
 */
function verVideoHD(videotag,randomid) {
      var registrado = 1;

      if (registrado != 1) {
        gen_output("<div align='center' class='video-youtubehd'><div class='video-container'><iframe type='text/html' width='853' height='505' src='http://widget.smartycenter.com/webservice/directYoutube/8982/" + videotag + "/853/505' frameborder='0' allowFullScreen scrolling='no'></iframe></div></div>",randomid);
      }

      else {
        gen_output("<div align='center' class='video-youtubehd'><div class='video-container'><iframe title='YouTube video player' class='youtube-player' type='text/html' width='853' height='510' src='//www.youtube.com/embed/" + videotag + "' frameborder='0' allowFullScreen></iframe></div></div>",randomid);
      }
}