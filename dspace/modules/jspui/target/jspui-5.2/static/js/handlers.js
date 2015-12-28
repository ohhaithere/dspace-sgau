$( document ).ready(function() {

    $( "#metadata_export_omg" ).click(function( event ) {
        var arr = [];
        $('.exportThisItem').each(function(i, obj) {
            arr.push(obj.id);
        });

        $.ajax({
            async: false,
            type: "POST",
            contentType: "application/json",
            url: "browse?type=title&submit_export_metadata=Export+metadata",
            data: JSON.stringify(arr),
            dataType : "json",
            success: function(response){
            }
        });
        return;
    });

});