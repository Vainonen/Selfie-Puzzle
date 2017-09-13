//var filepath = Android.getImageUri();
//var portrait = document.getElementById("portrait");
//portrait.src = filepath;
var pieces = shuffle(16);
var table = document.getElementById("table");
var puzzle = Array(16);

for (var i = 0; i < 4; i++) {
    var row = table.insertRow(i);
    for (var j = 0; j < 4; j++) {
        var cell = row.insertCell(j);
            var cellId = "cell" + (i * 4 + j);
            cell.id = cellId;
            var image = document.createElement('img');
            var x = i * -80;
            var y = j * -40;


            //image.style.backgroundPosition=x+'px '+y+' px';
            //$("#target").css('background-position', x+'px '+y+'px');

       image.setAttribute("height", "40");
        image.setAttribute("width", "80");
            image.position = cellId;
            image.id = pieces[i * 4 + j];
            cell.appendChild(image);
            //document.getElementById(image.id).style.backgroundImage = "url('" + filepath + "')";
            //"url(\'/" + imageArray[imageCounter].toString() + "\')";
            //document.getElementById(image.id).style.backgroundImage = "url(http://i.stack.imgur.com/ArS4Q.jpg)";
            document.getElementById(image.id).style.backgroundPosition = x+"px "+y+"px";
    }
}
var table = document.getElementById("puzzle");
    for (var i = 0; i < 4; i++) {
      var row = table.insertRow(i);
      for (var j = 0; j < 4; j++) {
        var cell = row.insertCell(j);
         var cellId = "puzzle" + (i * 4 + j);
            cell.id = cellId;
      }
    }

 $("img").dblclick(function(event, ui){
    $(this).appendTo("#"+this.position);
    $(this).draggable('enable');
    var containerId = $(this).attr("container");
    $('#'+containerId).droppable( "option", "disabled", false );
 });

  $( function() {
    $( "img" ).draggable({ revert: 'invalid', helper:'clone'});
    $( "#puzzle td" ).droppable({
      //tolerance: "fit",
      drop: function( event, ui ) {
        var number = parseInt(this.id.replace( /^\D+/g, ''));
        puzzle[number] = $(ui.draggable).attr('id');
        if (checkPuzzle(puzzle)) alert("oikein");
        //$(ui.draggable).draggable('disable');
        $(ui.draggable).attr("container", this.id);
        //$(this).droppable( "option", "disabled", true );
        $(this).append(ui.draggable.css('position','static'));
        $( this )
          .addClass( "ui-state-highlight" )

      }
    });
  });

  function shuffle(pieces) {
   var shuffled = new Array(pieces);
    for (var i = 0; i < pieces; i++) {
     shuffled[i] = i;
    }
    for (var i = pieces; i > 0; i--) {
        var j = Math.floor(Math.random() * i);
        var x = shuffled[i - 1];
        shuffled[i - 1] = shuffled[j];
        shuffled[j] = x;
    }
    return shuffled;
  }

  function checkPuzzle(puzzle) {
    var correctPieces = 0;
   for (var i = 0; i < puzzle.length; i++) {
     if (puzzle[i] == i) correctPieces++;
    }
    if (correctPieces == puzzle.length) return true;
   return false;
  }