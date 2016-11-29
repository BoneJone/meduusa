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
	
	