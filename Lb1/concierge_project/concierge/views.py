from django.shortcuts import render, redirect, get_object_or_404
from .models import Visitor
from django.utils import timezone
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth import login
from django.contrib.auth.decorators import login_required

@login_required
def visitor_list(request):
    visitors = Visitor.objects.all().order_by('-entry_time')
    return render(request, 'concierge/visitor_list.html', {'visitors': visitors})

@login_required
def add_visitor(request):
    if request.method == 'POST':
        full_name = request.POST.get('full_name')

        Visitor.objects.create(
            full_name=full_name
        )

        return redirect('visitor_list')

    return render(request, 'concierge/add_visitor.html')

@login_required
def exit_visitor(request, visitor_id):
    visitor = get_object_or_404(Visitor, id=visitor_id)

    visitor.is_inside = False
    visitor.exit_time = timezone.now()
    visitor.save()

    return redirect('visitor_list')

@login_required
def register(request):

    if request.method == 'POST':

        form = UserCreationForm(request.POST)

        if form.is_valid():
            user = form.save()

            login(request, user)

            return redirect('visitor_list')

    else:
        form = UserCreationForm()

    return render(
        request,
        'registration/register.html',
        {'form': form}
    )