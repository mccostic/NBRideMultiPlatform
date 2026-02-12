import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.dovoh.android_mvi.feature.home.data.model.CameraPosition
import platform.CoreGraphics.CGRect
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegate
import platform.MapKit.MKCoordinateRegion
import platform.MapKit.MKCoordinateSpan
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import kotlin.reflect.KFunction1

@Composable
fun InteractiveMapView(onCameraPositionChanged: KFunction1<CameraPosition, Unit>) {
    // Use a remember to hold the state of MKMapView
    val mapView = remember { MKMapView() }
    val viewController = UIViewController() 
    
    // Set up mapView properties
    mapView.delegate = object : MKMapViewDelegate() {
        override fun mapView(mapView: MKMapView, regionDidChangeAnimated:animated: Boolean) {
            val centerCoordinate = mapView.centerCoordinate
            val position = CameraPosition(centerCoordinate.latitude, centerCoordinate.longitude)
            // Report camera position changes
            onCameraPositionChanged(position)
        }
    }

    LaunchedEffect(Unit) {
        // Set and display map view
        val initialRegion = MKCoordinateRegion(
            mapView.centerCoordinate,
            MKCoordinateSpan(0.01, 0.01)
        )
        mapView.setRegion(initialRegion, true)
    }
    
    // Set UIView to the parent view controller
    viewController.view.addSubview(mapView)
    mapView.frame = CGRect(0.0, 0.0, viewController.view.bounds.size.width, viewController.view.bounds.size.height)
}