from django.urls import path

from . import views

urlpatterns = [

    path(
        '',
        views.home,
        name='home'
    ),

    path(
        'available-rooms/',
        views.available_rooms,
        name='available_rooms'
    ),

    path(
        'create-booking/',
        views.create_booking,
        name='create_booking'
    ),

    path(
        'delete-booking/<int:id>/',
        views.delete_booking,
        name='delete_booking'
    ),
]