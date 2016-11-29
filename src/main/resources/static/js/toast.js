	$('#testibutton').click(function(){
		$.toast({
			heading: 'Merkintä onnistui',
			text: 'Tuntien lisäys projektiin onnistui',
			icon: 'success',
			loader: false,
			position: {
				top: 60,
				right: 5,
			}
		});
	});
	
	
// Toast funktio

var naytaToast = function(otsikko, viesti, tyyppi) {
	$.toast({
		heading: otsikko,
		text: viesti,
		icon: tyyppi,
		loader: false,
		position: {
			top: 60,
			right: 5,
		}
	});
}