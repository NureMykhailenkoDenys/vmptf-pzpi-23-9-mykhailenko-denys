from django.urls import path
from . import views

urlpatterns = [
    path('', views.visitor_list, name='visitor_list'),
    path('add/', views.add_visitor, name='add_visitor'),
    path('exit/<int:visitor_id>/', views.exit_visitor, name='exit_visitor'),
    path('register/', views.register, name='register'),
]