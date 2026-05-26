from django.db import models


class Visitor(models.Model):
    full_name = models.CharField(max_length=100)
    entry_time = models.DateTimeField(auto_now_add=True)
    exit_time = models.DateTimeField(null=True, blank=True)
    is_inside = models.BooleanField(default=True)

    def __str__(self):
        return self.full_name